jQuery.extend(jQuery.validator.messages, {
    required: "Esse campo é requerido.",
    remote: "Please fix this field.",
    email: "Favor informar um email válido.",
    url: "Favor informar uma URL válida.",
    date: "Favor informar uma data válida.",
    dateISO: "Please enter a valid date (ISO).",
    number: "Favor informar um número v�válido.",
    digits: "Favor informar apenas dígitos.",
    creditcard: "Favor informar um número de cartão de crédito válido.",
    equalTo: "Favor informar o mesmo valor novamente.",
    accept: "Please enter a value with a valid extension.",
    maxlength: jQuery.validator.format("Favor informar no máximo {0} caracteres."),
    minlength: jQuery.validator.format("Favor informar no mínimo {0} caracteres."),
    rangelength: jQuery.validator.format("Favor informar um valor com tamanho entre {0} e {1} caracteres."),
    range: jQuery.validator.format("Favor informar um valor entre {0} e {1}."),
    max: jQuery.validator.format("Favor informar um valor menor ou igual a {0}."),
    min: jQuery.validator.format("Favor informar um valor maior ou igual a {0}.")
});