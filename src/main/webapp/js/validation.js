/**
 * Validation scripts for web interface inputs
 */

function setFieldError(field, isError, message) {
    if (!field) return;
    const parent = field.parentElement;
    let errSpan = parent.querySelector('.error-message');
    
    if (!errSpan) {
        errSpan = document.createElement('span');
        errSpan.className = 'error-message';
        parent.appendChild(errSpan);
    }
    
    if (isError) {
        field.classList.add('input-invalid');
        errSpan.textContent = message || 'Campo inválido';
        errSpan.style.display = 'block';
    } else {
        field.classList.remove('input-invalid');
        errSpan.style.display = 'none';
    }
}

function isValidCPF(cpf) {
    if (!cpf) return false;
    cpf = cpf.replace(/[^\d]+/g, '');
    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

    let soma = 0;
    let resto;
    for (let i = 1; i <= 9; i++) {
        soma = soma + parseInt(cpf.substring(i - 1, i)) * (11 - i);
    }
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.substring(9, 10))) return false;

    soma = 0;
    for (let i = 1; i <= 10; i++) {
        soma = soma + parseInt(cpf.substring(i - 1, i)) * (12 - i);
    }
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.substring(10, 11))) return false;

    return true;
}

function validateCidadeForm(form) {
    let isValid = true;
    const nome = form.querySelector('#nome');
    const uf = form.querySelector('#uf');

    if (!nome.value || nome.value.trim().length < 2) {
        setFieldError(nome, true, 'O nome da cidade deve ter pelo menos 2 caracteres.');
        isValid = false;
    } else {
        setFieldError(nome, false);
    }

    if (!uf.value || uf.value.trim().length !== 2) {
        setFieldError(uf, true, 'A UF deve ter exatamente 2 letras (ex: SP, RJ).');
        isValid = false;
    } else {
        setFieldError(uf, false);
    }

    return isValid;
}

function validateCategoriaForm(form) {
    let isValid = true;
    const nome = form.querySelector('#nome');

    if (!nome.value || nome.value.trim().length < 2) {
        setFieldError(nome, true, 'O nome da categoria é obrigatório.');
        isValid = false;
    } else {
        setFieldError(nome, false);
    }

    return isValid;
}

function validateClienteForm(form) {
    let isValid = true;
    const nome = form.querySelector('#nome');
    const cpf = form.querySelector('#cpf');
    const cidadeId = form.querySelector('#cidadeId');

    if (!nome.value || nome.value.trim().length < 3) {
        setFieldError(nome, true, 'Informe o nome completo do cliente (mínimo 3 letras).');
        isValid = false;
    } else {
        setFieldError(nome, false);
    }

    if (!cpf.value || !isValidCPF(cpf.value)) {
        setFieldError(cpf, true, 'CPF inválido. Digite um CPF válido.');
        isValid = false;
    } else {
        setFieldError(cpf, false);
    }

    if (!cidadeId || !cidadeId.value) {
        setFieldError(cidadeId, true, 'Selecione uma cidade.');
        isValid = false;
    } else {
        setFieldError(cidadeId, false);
    }

    return isValid;
}

function validateProdutoForm(form) {
    let isValid = true;
    const descricao = form.querySelector('#descricao');
    const unidade = form.querySelector('#unidade');
    const precoCusto = form.querySelector('#precoCusto');
    const precoVenda = form.querySelector('#precoVenda');
    const quantidade = form.querySelector('#quantidade');
    const categoriaId = form.querySelector('#categoriaId');

    if (!descricao.value || descricao.value.trim().length < 2) {
        setFieldError(descricao, true, 'A descrição do produto é obrigatória.');
        isValid = false;
    } else {
        setFieldError(descricao, false);
    }

    if (!unidade.value) {
        setFieldError(unidade, true, 'Selecione a unidade de medida.');
        isValid = false;
    } else {
        setFieldError(unidade, false);
    }

    const valCusto = parseFloat(precoCusto.value ? precoCusto.value.replace(',', '.') : '0');
    if (isNaN(valCusto) || valCusto < 0) {
        setFieldError(precoCusto, true, 'Preço de custo deve ser um número maior ou igual a zero.');
        isValid = false;
    } else {
        setFieldError(precoCusto, false);
    }

    const valVenda = parseFloat(precoVenda.value ? precoVenda.value.replace(',', '.') : '0');
    if (isNaN(valVenda) || valVenda <= 0) {
        setFieldError(precoVenda, true, 'Preço de venda deve ser maior que zero.');
        isValid = false;
    } else {
        setFieldError(precoVenda, false);
    }

    const valQtd = parseFloat(quantidade.value ? quantidade.value.replace(',', '.') : '0');
    if (isNaN(valQtd) || valQtd < 0) {
        setFieldError(quantidade, true, 'Quantidade em estoque não pode ser negativa.');
        isValid = false;
    } else {
        setFieldError(quantidade, false);
    }

    if (!categoriaId || !categoriaId.value) {
        setFieldError(categoriaId, true, 'Selecione uma categoria.');
        isValid = false;
    } else {
        setFieldError(categoriaId, false);
    }

    return isValid;
}

function validatePedidoForm(form) {
    let isValid = true;
    const clienteId = form.querySelector('#clienteId');

    if (!clienteId || !clienteId.value) {
        setFieldError(clienteId, true, 'Selecione o cliente para o pedido.');
        isValid = false;
    } else {
        setFieldError(clienteId, false);
    }

    return isValid;
}

function validateItemPedidoForm(form) {
    let isValid = true;
    const produtoId = form.querySelector('#produtoId');
    const quantidade = form.querySelector('#quantidade');
    const precoVenda = form.querySelector('#precoVenda');

    if (!produtoId || !produtoId.value) {
        setFieldError(produtoId, true, 'Selecione ou busque um produto.');
        isValid = false;
    } else {
        setFieldError(produtoId, false);
    }

    const valQtd = parseFloat(quantidade.value ? quantidade.value.replace(',', '.') : '0');
    if (isNaN(valQtd) || valQtd <= 0) {
        setFieldError(quantidade, true, 'Quantidade deve ser maior que zero.');
        isValid = false;
    } else {
        setFieldError(quantidade, false);
    }

    const valPreco = parseFloat(precoVenda.value ? precoVenda.value.replace(',', '.') : '0');
    if (isNaN(valPreco) || valPreco <= 0) {
        setFieldError(precoVenda, true, 'Preço de venda deve ser maior que zero.');
        isValid = false;
    } else {
        setFieldError(precoVenda, false);
    }

    return isValid;
}
