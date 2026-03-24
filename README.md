# AutoSales

AutoSales — веб-приложение автосалона, разработанное в учебных целях на Spring Boot и MySQL.

Проект включает пользовательскую часть, профиль, избранное, оформление покупки и административную панель.

## Основной функционал

### Для пользователя
- регистрация и авторизация
- просмотр каталога автомобилей
- поиск и фильтрация автомобилей
- просмотр карточки автомобиля
- добавление и удаление автомобилей из избранного
- оформление покупки
- просмотр профиля и редактирование личных данных
- просмотр списка покупок и деталей заказа

### Для администратора
- управление автомобилями
- управление марками и моделями
- управление пользователями
- управление продажами
- загрузка и редактирование фотографий автомобилей

## Технологии

- Java
- Spring Boot
- Spring Security
- Thymeleaf
- JDBC
- MySQL
- HTML
- CSS
- JavaScript

## Скриншоты проекта

### Главная страница / каталог
<p align="center">
  <img width="1442" height="650" alt="image" src="https://github.com/user-attachments/assets/cf7fdd66-0494-49bc-b1e9-427a98e12589" width="500" />
</p>

### Карточка автомобиля
<p align="center">
  <img width="1442" height="654" alt="image" src="https://github.com/user-attachments/assets/a45c3c50-c261-46d7-ad2b-52018160c14a" width="500" />
</p>

### Избранное
<p align="center">
  <img width="1442" height="652" alt="image" src="https://github.com/user-attachments/assets/41be9140-8bef-40e9-ba1d-68ea4ff9f64b" width="500" />
</p>

### Профиль пользователя
<p align="center">
   <img width="1442" height="654" alt="image" src="https://github.com/user-attachments/assets/f30cc35f-f4d4-43c1-92b6-28bdbc94abcb" width="500" />
</p>

### Админ-панель
<p align="center">
  <img width="1442" height="647" alt="image" src="https://github.com/user-attachments/assets/4c0ea155-85df-4e52-a088-225835c17cd2" width="500" />
</p>

## База данных

В проекте присутствует дамп базы данных:

📁 database/autosales_dump.sql

Содержит:
- структуру таблиц
- тестовые данные (пользователи, автомобили, заказы)

Для развёртывания:
1. Создать БД autosales
2. Выполнить SQL-скрипт

## Запуск проекта

1. Клонировать репозиторий
2. Настроить базу данных MySQL
3. Запустить проект через Spring Boot
4. Открыть в браузере: `http://localhost:8080`

## QA-портфолио по проекту

На основе AutoSales также подготовлено QA-портфолио с:
- чек-листом
- тест-кейсами
- баг-репортами
- API-тестами
- UI-автотестами на Cypress
- анализом HTTP-запросов

Ссылка на QA-портфолио: https://github.com/dimavarlamov/QA-portfolio
