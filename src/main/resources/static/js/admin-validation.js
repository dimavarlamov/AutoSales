document.addEventListener('DOMContentLoaded', function () {
    const fields = document.querySelectorAll('[data-max-length], [data-non-negative], [data-integer], [data-allowed-pattern]');

    fields.forEach(function (field) {
        const maxLength = field.dataset.maxLength ? Number(field.dataset.maxLength) : null;
        const fieldName = field.dataset.fieldName || 'Поле';

        function validateField() {
            field.setCustomValidity('');

            if (maxLength !== null && field.value.length > maxLength) {
                field.setCustomValidity(
                    fieldName + ': слишком длинное значение. Максимум ' + maxLength + ' символов.'
                );
                return;
            }

            if (field.dataset.nonNegative === 'true' && field.value !== '') {
                const numberValue = Number(field.value);

                if (Number.isNaN(numberValue)) {
                    field.setCustomValidity(fieldName + ': введите корректное число.');
                    return;
                }

                if (numberValue < 0) {
                    field.setCustomValidity(fieldName + ': значение не может быть отрицательным.');
                    return;
                }
            }

            if (field.dataset.integer === 'true' && field.value !== '') {
                const numberValue = Number(field.value);

                if (!Number.isInteger(numberValue)) {
                    field.setCustomValidity(fieldName + ': значение должно быть целым числом.');
                    return;
                }
            }

            if (field.dataset.allowedPattern && field.value !== '') {
                const pattern = new RegExp(field.dataset.allowedPattern);

                if (!pattern.test(field.value)) {
                    field.setCustomValidity(fieldName + ': поле содержит недопустимые символы.');
                    return;
                }
            }
        }

        field.addEventListener('input', validateField);

        field.addEventListener('blur', function () {
            validateField();

            if (!field.checkValidity()) {
                field.reportValidity();
            }
        });

        field.addEventListener('invalid', validateField);

        validateField();
    });
});