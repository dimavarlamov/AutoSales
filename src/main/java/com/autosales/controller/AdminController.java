package com.autosales.controller;

import com.autosales.dao.*;
import com.autosales.model.*;
import com.autosales.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import java.security.Principal;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserDao userDao;
    private final CarService carService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final SaleDao saleDao;
    private final SaleDetailDao saleDetailDao;
    private final FileStorageService fileStorageService;
    private final CarSpecificationDao carSpecificationDao;
    private final UserService userService;
    private final AuditLogService auditLogService;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(@RequestParam(required = false) String search,
                        Model model,
                        Principal principal) {
        List<User> users = adminService.getAllUsers();

        model.addAttribute("users", users);
        addCurrentUserIdToModel(model, principal);

        return "admin/users/list";
    }

    @GetMapping("/users/{id}")
    public String userDetails(@PathVariable Integer id,
                              Model model,
                              Principal principal) {
        User user = adminService.getUserById(id);

        model.addAttribute("user", user);
        addCurrentUserIdToModel(model, principal);

        return "admin/users/details";
    }

    @PostMapping("/users/{id}/role")
    public String updateUserRole(@PathVariable Integer id, @RequestParam Integer roleId,
                                 RedirectAttributes redirectAttributes, HttpServletRequest request) {
        User user = adminService.getUserById(id);
        Integer oldRoleId = user.getRoleId();
        adminService.updateUserRole(id, roleId);
        auditLogService.logAction("UPDATE_ROLE", "users", id,
                "Роль ID=" + oldRoleId, "Роль ID=" + roleId, request);
        redirectAttributes.addFlashAttribute("success", "Роль пользователя обновлена");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/balance")
    public String updateUserBalance(@PathVariable Integer id, @RequestParam BigDecimal balance,
                                    RedirectAttributes redirectAttributes, HttpServletRequest request) {
        User user = adminService.getUserById(id);
        BigDecimal oldBalance = user.getBalance();
        try {
            userService.updateBalance(id, balance);
            auditLogService.logAction("UPDATE_BALANCE", "users", id,
                    "Баланс: " + oldBalance, "Баланс: " + balance, request);
            redirectAttributes.addFlashAttribute("success", "Баланс пользователя обновлён");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users/" + id;
    }

    @GetMapping("/cars")
    public String cars(@RequestParam(required = false) String search,
                       @RequestParam(required = false) Integer brandId,
                       @RequestParam(required = false) BigDecimal minPrice,
                       @RequestParam(required = false) BigDecimal maxPrice,
                       @RequestParam(defaultValue = "0") int page,
                       Model model) {
        int size = 10;
        List<Car> cars = carService.findCarsWithFilters(search, brandId, minPrice, maxPrice, page, size);
        int total = carService.countCarsWithFilters(search, brandId, minPrice, maxPrice);
        int totalPages = (int) Math.ceil((double) total / size);

        model.addAttribute("cars", cars);
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedBrandId", brandId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "admin/cars/list";
    }

    @GetMapping("/cars/new")
    public String newCarForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("selectedBrandId", null);
        model.addAttribute("models", List.of());
        return "admin/cars/form";
    }

    @GetMapping("/cars/edit/{id}")
    public String editCarForm(@PathVariable Integer id, Model model) {
        Car car = carService.getCarById(id);
        model.addAttribute("car", car);
        CarModel carModel = modelService.getModelById(car.getModelId());
        model.addAttribute("selectedBrandId", carModel.getBrandId());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("models", modelService.getModelsByBrand(carModel.getBrandId()));

        CarSpecification spec = carSpecificationDao.findByCarId(id).orElse(null);
        model.addAttribute("spec", spec);

        return "admin/cars/form";
    }

    @PostMapping("/cars/save")
    public String saveCar(@ModelAttribute Car car,
                          @RequestParam Integer modelId,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          RedirectAttributes redirectAttributes) {
        try {
            car.setModelId(modelId);
            if (!imageFile.isEmpty()) {
                String imagePath = fileStorageService.storeFile(imageFile);
                car.setImage(imagePath);
            }
            carService.saveCar(car);
            redirectAttributes.addFlashAttribute("success", "Автомобиль добавлен");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке файла: " + e.getMessage());
        }
        return "redirect:/admin/cars";
    }

    @PostMapping("/cars/update")
    public String updateCar(@ModelAttribute Car car,
                            @RequestParam Integer modelId,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            RedirectAttributes redirectAttributes) {
        try {
            Car existingCar = carService.getCarById(car.getId());

            existingCar.setModelId(modelId);
            existingCar.setPrice(car.getPrice());
            existingCar.setStockQuantity(car.getStockQuantity());
            existingCar.setExpectedDate(car.getExpectedDate());
            existingCar.setDescription(car.getDescription());

            if (!imageFile.isEmpty()) {

                String imagePath = fileStorageService.storeFile(imageFile);
                existingCar.setImage(imagePath);
            }


            carService.updateCar(existingCar);
            redirectAttributes.addFlashAttribute("success", "Автомобиль обновлён");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке файла: " + e.getMessage());
        }
        return "redirect:/admin/cars";
    }

    @PostMapping("/cars/delete/{id}")
    public String deleteCar(@PathVariable Integer id,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {

        Car car = carService.getCarById(id);
        String carName = (car.getBrandName() + " " + car.getModelName()).trim();

        if (adminService.isCarLinkedToSale(id)) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Нельзя удалить автомобиль «" + carName + "», потому что он уже забронирован."
            );

            auditLogService.logAction(
                    "DELETE_DENIED",
                    "cars",
                    id,
                    "Попытка удаления автомобиля ID=" + id + ", название: " + carName,
                    "Удаление запрещено: автомобиль связан с продажей",
                    request
            );

            return "redirect:/admin/cars";
        }

        try {
            carService.deleteCar(id);

            auditLogService.logAction(
                    "DELETE",
                    "cars",
                    id,
                    "Автомобиль ID=" + id + ", название: " + carName,
                    null,
                    request
            );

            redirectAttributes.addFlashAttribute("success", "Автомобиль удалён");

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Нельзя удалить автомобиль «" + carName + "», потому что он связан с продажей или другим объектом базы данных."
            );

            auditLogService.logAction(
                    "DELETE_DENIED",
                    "cars",
                    id,
                    "Попытка удаления автомобиля ID=" + id + ", название: " + carName,
                    "Удаление запрещено ограничением внешнего ключа",
                    request
            );
        }

        return "redirect:/admin/cars";
    }

    @GetMapping("/brands")
    public String brands(Model model) {
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/brands/list";
    }

    @GetMapping("/brands/new")
    public String newBrandForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "admin/brands/form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(@ModelAttribute Brand brand, RedirectAttributes redirectAttributes) {
        brandService.saveBrand(brand);
        redirectAttributes.addFlashAttribute("success", "Марка добавлена");
        return "redirect:/admin/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrandForm(@PathVariable Integer id, Model model) {
        Brand brand = brandService.getBrandById(id);
        model.addAttribute("brand", brand);
        return "admin/brands/form";
    }

    @PostMapping("/brands/update")
    public String updateBrand(@ModelAttribute Brand brand, RedirectAttributes redirectAttributes) {
        brandService.updateBrand(brand);
        redirectAttributes.addFlashAttribute("success", "Марка обновлена");
        return "redirect:/admin/brands";
    }

    @PostMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Brand brand = brandService.getBrandById(id);
        auditLogService.logAction("DELETE", "brands", id,
                "Марка: " + brand.getName(), null, request);
        brandService.deleteBrand(id);
        redirectAttributes.addFlashAttribute("success", "Марка удалена");
        return "redirect:/admin/brands";
    }

    @GetMapping("/models")
    public String models(@RequestParam(required = false) Integer brandId, Model model) {
        List<CarModel> models;
        if (brandId != null) {
            models = modelService.getModelsByBrand(brandId);
        } else {
            models = modelService.getAllModels();
        }
        model.addAttribute("models", models);
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("selectedBrandId", brandId);
        return "admin/models/list";
    }

    @GetMapping("/models/new")
    public String newModelForm(Model model) {
        model.addAttribute("model", new CarModel());
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/models/form";
    }

    @PostMapping("/models/save")
    public String saveModel(@ModelAttribute CarModel model, RedirectAttributes redirectAttributes) {
        modelService.saveModel(model);
        redirectAttributes.addFlashAttribute("success", "Модель добавлена");
        return "redirect:/admin/models";
    }

    @GetMapping("/models/edit/{id}")
    public String editModelForm(@PathVariable Integer id, Model model) {
        CarModel carModel = modelService.getModelById(id);
        model.addAttribute("model", carModel);
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/models/form";
    }

    @PostMapping("/models/update")
    public String updateModel(@ModelAttribute CarModel model, RedirectAttributes redirectAttributes) {
        modelService.updateModel(model);
        redirectAttributes.addFlashAttribute("success", "Модель обновлена");
        return "redirect:/admin/models";
    }

    @PostMapping("/models/delete/{id}")
    public String deleteModel(@PathVariable Integer id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        CarModel model = modelService.getModelById(id);
        auditLogService.logAction("DELETE", "models", id,
                "Модель: " + model.getName() + " (ID марки " + model.getBrandId() + ")", null, request);
        modelService.deleteModel(id);
        redirectAttributes.addFlashAttribute("success", "Модель удалена");
        return "redirect:/admin/models";
    }

    @GetMapping("/sales")
    public String sales(@RequestParam(required = false) BigDecimal minAmount,
                        @RequestParam(required = false) BigDecimal maxAmount,
                        @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate startDate,
                        @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate endDate,
                        @RequestParam(required = false) Integer brandId,
                        @RequestParam(required = false) Integer modelId,
                        Model model) {
        List<Map<String, Object>> sales = adminService.getSalesWithFilters(minAmount, maxAmount, startDate, endDate, brandId, modelId);
        model.addAttribute("sales", sales);
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("models", modelId != null || brandId != null
                ? modelService.getAllModels()
                : modelService.getAllModels());
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedBrandId", brandId);
        model.addAttribute("selectedModelId", modelId);
        return "admin/sales/list";
    }

    @GetMapping("/sales/{id}")
    public String saleDetails(@PathVariable Integer id, Model model) {
        Sale sale = saleDao.findById(id).orElseThrow();
        model.addAttribute("sale", sale);
        return "admin/sales/details";
    }

    @GetMapping("/reports/availability")
    public String reportAvailability(@RequestParam(required = false) String brandName,
                                     @RequestParam(required = false) String modelName,
                                     Model model) {
        if (brandName != null && modelName != null) {
            List<Map<String, Object>> cars = adminService.getCarsInStockByBrandAndModel(brandName, modelName);
            model.addAttribute("cars", cars);
        }
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/reports/availability";
    }

    @GetMapping("/reports/techdata")
    public String reportTechData(@RequestParam(required = false) Integer modelId, Model model) {
        if (modelId != null) {
        }
        model.addAttribute("models", modelService.getAllModels());
        return "admin/reports/techdata";
    }

    @GetMapping("/reports/sold-by-brand")
    public String reportSoldByBrand(@RequestParam(required = false) Integer brandId, Model model) {
        if (brandId != null) {
            List<Sale> sales = adminService.getSoldCarsByBrand(brandId);
            model.addAttribute("sales", sales);
        }
        model.addAttribute("brands", brandService.getAllBrands());
        return "admin/reports/sold-by-brand";
    }

    @GetMapping("/reports/sales-summary")
    public String reportSalesSummary(Model model) {
        Map<String, Object> summary = adminService.getSalesSummary();
        model.addAttribute("summary", summary);
        return "admin/reports/sales-summary";
    }

    @GetMapping("/api/models/byBrand/{brandId}")
    @ResponseBody
    public List<CarModel> getModelsByBrand(@PathVariable Integer brandId) {
        return modelService.getModelsByBrand(brandId);
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUserEnabled(@PathVariable Integer id,
                                    RedirectAttributes redirectAttributes,
                                    HttpServletRequest request,
                                    Principal principal) {

        Integer currentUserId = getCurrentUserId(principal);

        if (currentUserId != null && currentUserId.equals(id)) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Нельзя заблокировать или разблокировать собственную учётную запись"
            );
            return "redirect:/admin/users/" + id;
        }

        User user = adminService.getUserById(id);

        boolean oldStatus = user.getEnabled();

        adminService.toggleUserEnabled(id);

        String newStatus = !oldStatus ? "активирован" : "заблокирован";

        auditLogService.logAction("TOGGLE_ENABLED", "users", id,
                "Был " + (oldStatus ? "активен" : "заблокирован"),
                "Стал " + newStatus, request);

        redirectAttributes.addFlashAttribute("success", "Статус пользователя изменён");

        return "redirect:/admin/users";
    }

    private Integer getCurrentUserId(Principal principal) {
        if (principal == null) {
            return null;
        }

        return userService.getUserByEmail(principal.getName()).getId();
    }

    private void addCurrentUserIdToModel(Model model, Principal principal) {
        model.addAttribute("currentUserId", getCurrentUserId(principal));
    }

}