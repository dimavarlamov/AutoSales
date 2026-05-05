document.addEventListener('DOMContentLoaded', function () {
    const fields = document.querySelectorAll('[data-max-length]');

    fields.forEach(function (field) {
        const maxLength = Number(field.dataset.maxLength);
        const fieldName = field.dataset.fieldName || 'Поле';

        function validateLength() {
            if (field.value.length > maxLength) {
                field.setCustomValidity(
                    fieldName + ': слишком длинное значение. Максимум ' + maxLength + ' символов.'
                );
            } else {
                field.setCustomValidity('');
            }
        }

        field.addEventListener('input', validateLength);

        field.addEventListener('blur', function () {
            validateLength();

            if (field.value.length > maxLength) {
                field.reportValidity();
            }
        });

        field.addEventListener('invalid', validateLength);

        validateLength();
    });
});