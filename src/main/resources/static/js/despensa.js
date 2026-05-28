/* ============================================================
     CONFIG
     ============================================================ */
const API_DESPENSA     = '/api/despensa';
const API_INGREDIENTES = '/api/ingredientes';
const DESPENSA_ID      = 1;

let ingredientes      = [];   // IngredienteDetalleDTO[]  { ingredienteId, descripcion, cantidadStock }
let todosIngredientes = [];   // Ingrediente[]            { id, descripcion, cantidadStock }
let selectedId        = null; // ingredienteId actualmente seleccionado en la tabla
let displayItems      = [];
let featuredNames     = ['Carne Vacuna', 'Tomate']; // configurables por el usuario

/* ============================================================
   CARGA
   GET /api/despensa/{id}/ingredientes → List<IngredienteDetalleDTO>
   ============================================================ */
async function cargarDespensa() {
    try {
        setStatus('Cargando despensa…', true);

        const res = await fetch(`${API_DESPENSA}/${DESPENSA_ID}/ingredientes`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        ingredientes = await res.json();
        displayItems = [...ingredientes];

        renderCards();
        renderTable(displayItems);

    } catch (err) {
        console.error('[cargarDespensa]', err);
        setStatus('✗ Error al cargar despensa', false);
    }
}

async function refreshDespensa() { await cargarDespensa(); }

/* ============================================================
   HELPERS
   ============================================================ */
function getStatus(stock) {
    if (stock === 0) return { cls: 'status-danger', text: 'Sin stock'  };
    if (stock < 10)  return { cls: 'status-warn',   text: 'Stock bajo' };
    return                  { cls: 'status-ok',     text: 'Disponible' };
}
function getCardClass(stock) {
    if (stock === 0) return 'danger';
    if (stock < 10)  return 'warn';
    return 'ok';
}

/* ============================================================
   TARJETAS DESTACADAS
   ============================================================ */
function renderCards() {
    const div = document.getElementById('stock-cards');

    const featured = featuredNames
        .map(n => ingredientes.find(
            i => i.descripcion.toLowerCase().includes(n.toLowerCase())
        ))
        .filter(Boolean);

    const cardsHTML = featured.map(i => `
      <div class="stock-card ${getCardClass(i.cantidadStock)}">
        <div class="stock-card-label">${i.descripcion}</div>
        <div class="stock-card-value">${i.cantidadStock}</div>
        <div class="stock-card-unit">unidades en stock</div>
      </div>
    `).join('');

    const configCard = `
      <div class="stock-card config-card" onclick="openConfigCards()" title="Configurar tarjetas destacadas">
        <span style="font-size:1.3rem;color:var(--teal);opacity:.6">⚙</span>
        <span style="font-size:.7rem;font-weight:600;color:var(--muted);text-align:center;line-height:1.5;text-transform:uppercase;letter-spacing:.08em">
          Configurar<br>destacados
        </span>
      </div>
    `;

    div.innerHTML = cardsHTML + configCard;
}

/* ============================================================
   TABLA
   ============================================================ */
function renderTable(items) {
    const tbody = document.getElementById('despensa-tbody');
    tbody.innerHTML = '';

    if (!items.length) {
        tbody.innerHTML = `
        <tr><td colspan="5"
          style="text-align:center;color:var(--muted);padding:32px;font-size:.83rem">
          Sin resultados para el filtro actual
        </td></tr>`;
        return;
    }

    items.forEach(ing => {
        const { cls, text } = getStatus(ing.cantidadStock);
        const tr = document.createElement('tr');

        if (selectedId === ing.ingredienteId) tr.classList.add('selected');

        tr.innerHTML = `
        <td class="td-id">${ing.ingredienteId}</td>
        <td class="td-name">${ing.descripcion}</td>
        <td class="td-stock">${ing.cantidadStock}
          <span style="color:var(--muted);font-size:.7rem;font-weight:400"> unidades</span>
        </td>
        <td><span class="status-badge ${cls}">${text}</span></td>
        <td>
          <button class="btn btn-ghost btn-sm" style="padding:4px 9px"
            onclick="quickUpdate(${ing.ingredienteId}, event)" title="Editar stock">✎</button>
        </td>
      `;

        tr.addEventListener('click', e => {
            if (e.target.tagName === 'BUTTON') return;
            selectRow(ing.ingredienteId, tr);
        });

        tbody.appendChild(tr);
    });

    document.getElementById('total-label').textContent =
        `${ingredientes.length} ingredientes`;
    setStatus(`✓ Despensa cargada — ${ingredientes.length} ingredientes`, true);
}

function selectRow(id, tr) {
    document.querySelectorAll('#despensa-tbody tr').forEach(r => r.classList.remove('selected'));
    if (selectedId === id) { selectedId = null; return; }
    selectedId = id;
    tr.classList.add('selected');
}

/* ============================================================
   FILTROS
   ============================================================ */
function applyFilter() {
    const q      = document.getElementById('search-input').value.toLowerCase();
    const filter = document.getElementById('filter-select').value;

    displayItems = ingredientes.filter(ing => {
        const matchQ = ing.descripcion.toLowerCase().includes(q);
        let   matchF = true;
        if (filter === 'ok')   matchF = ing.cantidadStock > 0;
        if (filter === 'low')  matchF = ing.cantidadStock > 0 && ing.cantidadStock < 10;
        if (filter === 'zero') matchF = ing.cantidadStock === 0;
        return matchQ && matchF;
    });

    renderTable(displayItems);
}

function clearFilters() {
    document.getElementById('search-input').value  = '';
    document.getElementById('filter-select').value = 'all';
    applyFilter();
}

/* ============================================================
   ACTUALIZAR STOCK
   PUT /api/ingredientes/{id}/stock?stock={value}
   ============================================================ */
function actualizarStock() {
    if (!selectedId) { alert('Seleccioná un ingrediente de la tabla primero.'); return; }
    const ing = ingredientes.find(i => i.ingredienteId === selectedId);
    document.getElementById('ms-nombre').textContent = ing.descripcion;
    document.getElementById('ms-actual').textContent = `${ing.cantidadStock} unidades`;
    document.getElementById('ms-nuevo').value = ing.cantidadStock;
    openModal('modal-stock');
}

function quickUpdate(id, e) {
    e.stopPropagation();
    selectedId = id;
    actualizarStock();
}

async function confirmarStock() {
    const nuevo = parseInt(document.getElementById('ms-nuevo').value, 10);
    if (isNaN(nuevo) || nuevo < 0) { alert('Ingresá un número válido ≥ 0.'); return; }

    try {
        // PUT /api/ingredientes/{ingredienteId}/stock?stock={nuevoValor}
        const res = await fetch(
            `${API_INGREDIENTES}/${selectedId}/stock?stock=${nuevo}`,
            { method: 'PUT' }
        );
        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        closeModal('modal-stock');
        await cargarDespensa();
        setStatus('✓ Stock actualizado correctamente', true);

    } catch (err) {
        console.error('[confirmarStock]', err);
        setStatus('✗ Error al actualizar stock', false);
    }
}

/* ── tab activo en el modal ── */
let tabActual = 'existente';

function setTab(tab) {
    tabActual = tab;

    document.getElementById('panel-existente').style.display = tab === 'existente' ? '' : 'none';
    document.getElementById('panel-nuevo').style.display     = tab === 'nuevo'     ? '' : 'none';

    const estiloActivo   = `background:var(--teal);color:#fff`;
    const estiloInactivo = `background:var(--bg-elevated);color:var(--muted)`;

    document.getElementById('tab-existente').style.cssText =
        tab === 'existente' ? estiloActivo : estiloInactivo;
    document.getElementById('tab-nuevo').style.cssText =
        tab === 'nuevo' ? estiloActivo : estiloInactivo;
}

/* ============================================================
   AGREGAR INGREDIENTE A DESPENSA
   GET /api/ingredientes                   → todos los ingredientes del sistema
   POST /api/despensa/{id}/ingredientes/{ingredienteId}
   ============================================================ */
/* ── abrir modal ── */
async function openAgregarIngModal() {
    try {
        const res = await fetch(API_INGREDIENTES);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        todosIngredientes = await res.json();

        const idsEnDespensa = new Set(ingredientes.map(i => i.ingredienteId));
        const disponibles   = todosIngredientes.filter(i => !idsEnDespensa.has(i.id));

        const sel = document.getElementById('ma-select');
        sel.innerHTML = disponibles.length
            ? disponibles
                .sort((a, b) => a.descripcion.localeCompare(b.descripcion))
                .map(i => `<option value="${i.id}">${i.descripcion} — Stock: ${i.cantidadStock} u.</option>`)
                .join('')
            : '<option disabled>Todos los ingredientes ya están en la despensa</option>';

        // Resetear al tab inicial
        setTab('existente');
        document.getElementById('nuevo-descripcion').value = '';
        document.getElementById('nuevo-stock').value       = '0';

        openModal('modal-agregar');

    } catch (err) {
        console.error('[openAgregarIngModal]', err);
        setStatus('✗ Error al cargar ingredientes disponibles', false);
    }
}

/* ── confirmar según el tab activo ── */
async function confirmarAgregar() {
    try {
        let ingredienteId;

        if (tabActual === 'existente') {
            // ── Caso 1: agregar uno ya existente ──────────────────────────
            const sel = document.getElementById('ma-select');
            if (!sel.value) { alert('Seleccioná un ingrediente.'); return; }
            ingredienteId = sel.value;

        } else {
            // ── Caso 2: crear uno nuevo y luego agregarlo ─────────────────
            const descripcion = document.getElementById('nuevo-descripcion').value.trim();
            const stock       = parseInt(document.getElementById('nuevo-stock').value, 10);

            if (!descripcion)        { alert('El nombre es obligatorio.'); return; }
            if (isNaN(stock) || stock < 0) { alert('El stock debe ser ≥ 0.'); return; }

            // POST /api/ingredientes  →  crea el ingrediente en el sistema
            const resCrear = await fetch(API_INGREDIENTES, {
                method:  'POST',
                headers: { 'Content-Type': 'application/json' },
                body:    JSON.stringify({ descripcion, cantidadStock: stock })
            });
            if (!resCrear.ok) throw new Error(`HTTP ${resCrear.status} al crear`);

            const nuevo = await resCrear.json();
            ingredienteId = nuevo.id;
        }

        // POST /api/despensa/{despensaId}/ingredientes/{ingredienteId}
        const resAgregar = await fetch(
            `${API_DESPENSA}/${DESPENSA_ID}/ingredientes/${ingredienteId}`,
            { method: 'POST' }
        );
        if (!resAgregar.ok) throw new Error(`HTTP ${resAgregar.status} al agregar`);

        closeModal('modal-agregar');
        await cargarDespensa();
        setStatus('✓ Ingrediente agregado a la despensa', true);

    } catch (err) {
        console.error('[confirmarAgregar]', err);
        setStatus('✗ Error al agregar ingrediente', false);
    }
}

/* ============================================================
   ELIMINAR INGREDIENTE DE DESPENSA
   DELETE /api/despensa/{id}/ingredientes/{ingredienteId}
   ============================================================ */
async function eliminarIngrediente() {
    if (!selectedId) { alert('Seleccioná un ingrediente de la tabla primero.'); return; }

    const ing = ingredientes.find(i => i.ingredienteId === selectedId);
    if (!confirm(
        `¿Eliminar "${ing.descripcion}" de esta despensa?\n\n` +
        `El ingrediente no se borra del sistema, solo se desvincula de esta despensa.`
    )) return;

    try {
        // DELETE /api/despensa/{despensaId}/ingredientes/{ingredienteId}
        const res = await fetch(
            `${API_DESPENSA}/${DESPENSA_ID}/ingredientes/${selectedId}`,
            { method: 'DELETE' }
        );
        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        selectedId = null;
        await cargarDespensa();
        setStatus(`✓ "${ing.descripcion}" eliminado de la despensa`, true);

    } catch (err) {
        console.error('[eliminarIngrediente]', err);
        setStatus('✗ Error al eliminar ingrediente', false);
    }
}

/* ============================================================
   STOCK BAJO
   GET /api/despensa/{id}/stock-bajo?umbral=10
   (o filtrado local si ya tenés los datos)
   ============================================================ */
function verStockBajo() {
    const bajos = ingredientes
        .filter(i => i.cantidadStock < 10)
        .sort((a, b) => a.cantidadStock - b.cantidadStock);

    const body = document.getElementById('alert-body');

    if (!bajos.length) {
        body.innerHTML = `
        <div style="text-align:center;padding:24px">
          <div style="font-size:2rem;margin-bottom:8px">✅</div>
          <p style="color:var(--muted);font-size:.84rem">Todos los ingredientes tienen stock suficiente.</p>
        </div>`;
    } else {
        body.innerHTML = bajos.map(ing => {
            const cls = ing.cantidadStock === 0 ? 'zero' : 'low';
            const lbl = ing.cantidadStock === 0 ? 'SIN STOCK' : `${ing.cantidadStock} unidades`;
            return `
          <div class="alert-item">
            <span class="alert-item-name">${ing.descripcion}</span>
            <span class="alert-item-stock ${cls}">${lbl}</span>
          </div>`;
        }).join('');
    }

    document.getElementById('alert-panel').classList.add('open');
}

function closeAlertPanel() {
    document.getElementById('alert-panel').classList.remove('open');
}

/* ============================================================
   CONFIGURAR TARJETAS DESTACADAS
   ============================================================ */
function openConfigCards() {
    const container = document.getElementById('config-ingredientes');

    container.innerHTML = ingredientes
        .slice()
        .sort((a, b) => a.descripcion.localeCompare(b.descripcion))
        .map(i => `
        <label>
          <input type="checkbox" value="${i.descripcion}"
            ${featuredNames.includes(i.descripcion) ? 'checked' : ''}>
          <span>${i.descripcion}</span>
          <span class="config-stock">${i.cantidadStock} u.</span>
        </label>
      `).join('');

    openModal('modal-config');
}

function confirmarConfig() {
    const checks  = document.querySelectorAll('#config-ingredientes input[type="checkbox"]:checked');
    featuredNames = [...checks].map(c => c.value);
    closeModal('modal-config');
    renderCards();
}

/* ============================================================
   MODAL HELPERS
   ============================================================ */
function openModal(id)  { document.getElementById(id).classList.add('open'); }
function closeModal(id) { document.getElementById(id).classList.remove('open'); }

document.querySelectorAll('.modal-backdrop').forEach(el =>
    el.addEventListener('click', e => { if (e.target === el) closeModal(el.id); })
);

/* ============================================================
   STATUS
   ============================================================ */
function setStatus(msg, ok) {
    document.getElementById('status-text').textContent = msg;
    document.getElementById('status-dot').className = 'status-dot' + (ok ? '' : ' error');
}

/* ============================================================
   INIT
   ============================================================ */
cargarDespensa();