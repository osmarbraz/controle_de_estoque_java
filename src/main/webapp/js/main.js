/**
 * Main application interactive behaviors and dynamic product searching
 */

document.addEventListener('DOMContentLoaded', function () {
    initProductSearch();
    initCPFFormatting();
});

/**
 * Real-time product search by description for ItemPedido screen
 */
function initProductSearch() {
    const searchInput = document.getElementById('produtoSearchInput');
    const resultsContainer = document.getElementById('produtoSearchResults');
    const produtoSelect = document.getElementById('produtoId');
    const precoInput = document.getElementById('precoVenda');
    const estoqueInfo = document.getElementById('estoqueDisponivelInfo');

    if (!searchInput || !resultsContainer) return;

    let debounceTimer;

    searchInput.addEventListener('input', function () {
        clearTimeout(debounceTimer);
        const query = this.value.trim();

        if (query.length < 1) {
            resultsContainer.style.display = 'none';
            return;
        }

        debounceTimer = setTimeout(() => {
            fetch(window.contextPath + '/servlet/produtos-search?q=' + encodeURIComponent(query))
                .then(res => res.json())
                .then(data => {
                    resultsContainer.innerHTML = '';
                    if (!data || data.length === 0) {
                        resultsContainer.innerHTML = '<div class="search-result-item" style="color: #64748b;">Nenhum produto encontrado</div>';
                    } else {
                        data.forEach(prod => {
                            const item = document.createElement('div');
                            item.className = 'search-result-item';
                            item.innerHTML = `
                                <div>
                                    <strong>${escapeHtml(prod.descricao)}</strong> 
                                    <small style="color: #64748b;">(${prod.unidade}) - Cat: ${escapeHtml(prod.categoriaNome || '')}</small>
                                </div>
                                <div style="text-align: right;">
                                    <strong style="color: #2563eb;">R$ ${formatCurrency(prod.precoVenda)}</strong><br>
                                    <small style="color: ${prod.quantidade > 0 ? '#10b981' : '#ef4444'};">Estoque: ${prod.quantidade}</small>
                                </div>
                            `;

                            item.addEventListener('click', function () {
                                selectProduct(prod);
                                resultsContainer.style.display = 'none';
                            });

                            resultsContainer.appendChild(item);
                        });
                    }
                    resultsContainer.style.display = 'block';
                })
                .catch(err => {
                    console.error('Erro ao buscar produtos:', err);
                });
        }, 250);
    });

    // Esconder resultados ao clicar fora
    document.addEventListener('click', function (e) {
        if (!searchInput.contains(e.target) && !resultsContainer.contains(e.target)) {
            resultsContainer.style.display = 'none';
        }
    });

    // Se o select de produtos mudar manualmente
    if (produtoSelect) {
        produtoSelect.addEventListener('change', function () {
            const selectedOpt = this.options[this.selectedIndex];
            if (selectedOpt && selectedOpt.dataset.preco) {
                if (precoInput) precoInput.value = Number.parseFloat(selectedOpt.dataset.preco).toFixed(2);
                if (estoqueInfo) estoqueInfo.textContent = 'Estoque disponível: ' + (selectedOpt.dataset.estoque || '0');
            }
        });
    }

    function selectProduct(prod) {
        if (produtoSelect) {
            produtoSelect.value = prod.produtoId;
        }
        if (precoInput) {
            precoInput.value = prod.precoVenda.toFixed(2);
        }
        if (searchInput) {
            searchInput.value = prod.descricao;
        }
        if (estoqueInfo) {
            estoqueInfo.textContent = 'Estoque disponível: ' + prod.quantidade;
        }
    }
}

function initCPFFormatting() {
    const cpfInputs = document.querySelectorAll('.mask-cpf');
    cpfInputs.forEach(input => {
        input.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 11) value = value.slice(0, 11);

            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');

            e.target.value = value;
        });
    });
}

function formatCurrency(val) {
    return Number(val).toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function escapeHtml(text) {
    if (!text) return '';
    return text.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}
