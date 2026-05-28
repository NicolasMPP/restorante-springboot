/* ══════════════════════════════════════════════════════════════
   menu.js  →  src/main/resources/static/js/menu.js
   ══════════════════════════════════════════════════════════════ */

const API_MENU         = '/api/menu';
const API_CHEFS        = '/api/chefs';
const API_INGREDIENTES = '/api/ingredientes';
const API_RECETAS      = '/api/recetas';
const MENU_ID          = 1;

let menuItems   = [];   // AlimentoDetalleDTO[]
let selectedRow = -1;

/* ══════════════════════════════════════════════════════════════
   CARGA INICIAL
   GET /api/menu/{id}           → Menu  (gerente info)
   GET /api/menu/{id}/alimentos → List<AlimentoDetalleDTO>
   ══════════════════════════════════════════════════════════════ */
async function cargarMenu() {
    try {
        setStatus('Cargando menú…', true);

        // Gerente
        const resMenu = await fetch(`${API_MENU}/${MENU_ID}`);
        if (resMenu.ok) {
            const menu = await resMenu.json();
            const g = menu.gerente;
            if (g) {
                document.getElementById('gerente-nombre').textContent = g.nombre ?? '—';
                document.getElementById('gerente-cedula').textContent = g.cedula ?? '—';
                document.getElementById('gerente-tel').textContent = g.telefono ?? '—';
                document.getElementById('gerente-email').textContent = g.correo ?? '—';
            }
        }

        // Alimentos
        const resAl = await fetch(`${API_MENU}/${MENU_ID}/alimentos`);
        if (!resAl.ok) throw new Error(`HTTP ${resAl.status}`);


        menuItems = await resAl.json();
        renderTable(menuItems);

    } catch (err) {
        console.error('[cargarMenu]', err);
        setStatus('✗ Error al cargar el menú', false);
    }
}

async function refreshMenu() { await cargarMenu(); }

/* ══════════════════════════════════════════════════════════════
   RENDER TABLA
   ══════════════════════════════════════════════════════════════ */
function renderTable(items) {
    const tbody = document.getElementById('menu-tbody');
    tbody.innerHTML = '';

    if (!items.length) {
        tbody.innerHTML = `
      <tr><td colspan="6"
        style="text-align:center;color:var(--muted);padding:36px;font-size:.83rem">
        El menú no tiene alimentos aún
      </td></tr>`;
        document.getElementById('count-label').textContent = '0 alimentos';
        setStatus('⚠ El menú está vacío', false);
        return;
    }

    items.forEach((it, i) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
      <td class="td-name">${it.alimentoNombre}</td>
      <td>${typeBadge(it.tipoAlimento)}</td>
      <td class="td-mono">$${Number(it.precio).toFixed(2)}</td>
      <td class="td-recipe">${it.nombreReceta ?? 'Sin receta'}</td>
      <td class="td-chef">${it.chefNombre ?? '—'}</td>
      <td><span class="ing-pill">${it.totalIngredientes ?? 0}</span></td>
    `;
        tr.addEventListener('click', () => selectRow(i, tr));
        tbody.appendChild(tr);
    });

    document.getElementById('count-label').textContent = `${items.length} alimentos`;
    setStatus(`✓ Menú cargado — ${items.length} alimentos`, true);
}

const typeBadge = t => {
    const map = {
        PLATO_FUERTE: ['badge-plato',     'Plato Fuerte'],
        POSTRE:       ['badge-postre',    'Postre'      ],
        BEBIDA:       ['badge-bebida',    'Bebida'      ],
        ADICIONAL:    ['badge-adicional', 'Adicional'   ],
    };
    const [cls, label] = map[t] || ['', t ?? '—'];
    return `<span class="badge ${cls}">${label}</span>`;
};

function selectRow(i, tr) {
    document.querySelectorAll('#menu-tbody tr').forEach(r => r.classList.remove('selected'));
    if (selectedRow === i) { selectedRow = -1; return; }
    selectedRow = i;
    tr.classList.add('selected');
}

function clearSelection() {
    selectedRow = -1;
    document.querySelectorAll('#menu-tbody tr').forEach(r => r.classList.remove('selected'));
}

/* ══════════════════════════════════════════════════════════════
   PANEL DETALLE — RECETA
   GET /api/recetas/{recetaId}  →  Receta con ingredientes
   ══════════════════════════════════════════════════════════════ */
async function verReceta() {
    if (selectedRow < 0) { alert('Seleccioná un alimento de la tabla.'); return; }

    const it = menuItems[selectedRow];

    if (!it.recetaId) {
        alert(`"${it.alimentoNombre}" no tiene receta asociada.`);
        return;
    }

    try {
        const res = await fetch(`${API_RECETAS}/${it.recetaId}`);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);
        const receta = await res.json();

        const ingTags = receta.ingredientes?.length
            ? receta.ingredientes.map(i =>
                `<span class="ing-tag">${i.descripcion}
            <span style="color:var(--muted);font-size:.68rem"> ${i.cantidadStock} u.</span>
          </span>`
            ).join('')
            : '<span style="color:var(--muted);font-size:.82rem">Sin ingredientes registrados</span>';

        document.getElementById('dp-title').textContent = it.alimentoNombre;
        document.getElementById('dp-body').innerHTML = `
      <div class="detail-section">
        <h4>Alimento</h4>
        <div class="detail-row"><span>Tipo</span>   <span>${it.tipoAlimento?.replace('_',' ')}</span></div>
        <div class="detail-row"><span>Precio</span> <span>$${Number(it.precio).toFixed(2)}</span></div>
      </div>
      <div class="detail-section">
        <h4>Receta — ${receta.nombreReceta}</h4>
        <div class="detail-row"><span>Chef</span> <span>${it.chefNombre ?? '—'}</span></div>
        <div class="detail-row"><span>Ingredientes</span> <span>${it.totalIngredientes ?? 0}</span></div>
      </div>
      <div class="detail-section">
        <h4>Proceso de preparación</h4>
        <div class="detail-process">${receta.descripcionProceso ?? 'Sin descripción'}</div>
      </div>
      <div class="detail-section">
        <h4>Ingredientes</h4>
        <div style="margin-top:4px">${ingTags}</div>
      </div>
    `;
        openDetail();

    } catch (err) {
        console.error('[verReceta]', err);
        alert('No se pudo cargar el detalle de la receta.');
    }
}

/* ══════════════════════════════════════════════════════════════
   PANEL DETALLE — CHEF
   GET /api/chefs/{chefId}          →  Chef
   GET /api/chefs/{chefId}/recetas  →  List<Receta>
   ══════════════════════════════════════════════════════════════ */
async function verChef() {
    if (selectedRow < 0) { alert('Seleccioná un alimento de la tabla.'); return; }

    const it = menuItems[selectedRow];

    if (!it.chefId) {
        alert(`"${it.alimentoNombre}" no tiene chef asignado.`);
        return;
    }

    try {
        const [resChef, resRecetas] = await Promise.all([
            fetch(`${API_CHEFS}/${it.chefId}`),
            fetch(`${API_CHEFS}/${it.chefId}/recetas`),
        ]);

        if (!resChef.ok) throw new Error(`HTTP ${resChef.status}`);
        const chef    = await resChef.json();
        const recetas = resRecetas.ok ? await resRecetas.json() : [];

        const recetaItems = recetas.length
            ? recetas.map(r => `
          <div class="detail-row">
            <span>${r.nombreReceta}</span>
            <span>${r.ingredientes?.length ?? 0} ing.</span>
          </div>`).join('')
            : '<span style="color:var(--muted);font-size:.82rem">Sin recetas registradas</span>';

        document.getElementById('dp-title').textContent = chef.nombre;
        document.getElementById('dp-body').innerHTML = `
      <div class="detail-section">
        <h4>Información personal</h4>
        <div class="detail-row"><span>Cédula</span>   <span>${chef.cedula   ?? '—'}</span></div>
        <div class="detail-row"><span>Teléfono</span> <span>${chef.telefono ?? '—'}</span></div>
        <div class="detail-row"><span>Correo</span>   <span>${chef.correo   ?? '—'}</span></div>
      </div>
      <div class="detail-section">
        <h4>Recetas asignadas (${recetas.length})</h4>
        ${recetaItems}
      </div>
    `;
        openDetail();

    } catch (err) {
        console.error('[verChef]', err);
        alert('No se pudo cargar la información del chef.');
    }
}

function openDetail()  { document.getElementById('detail-panel').classList.add('open'); }
function closeDetail() { document.getElementById('detail-panel').classList.remove('open'); }

/* ══════════════════════════════════════════════════════════════
   MODAL — AGREGAR ALIMENTO
   GET /api/ingredientes  →  poblar lista disponibles
   GET /api/chefs         →  poblar select de chef
   ══════════════════════════════════════════════════════════════ */
async function openAddModal() {
    hideModalErrors();

    // Limpiar estado anterior
    document.getElementById('f-nombre').value  = '';
    document.getElementById('f-precio').value  = '';
    document.getElementById('f-receta').value  = '';
    document.getElementById('f-proceso').value = '';
    document.getElementById('lst-seleccionados').innerHTML = '';
    updateIngCount();

    // Cargar chefs e ingredientes en paralelo
    const [resChefs, resIngs] = await Promise.allSettled([
        fetch(API_CHEFS),
        fetch(API_INGREDIENTES),
    ]);

    // Poblar select de chefs
    const selChef = document.getElementById('f-chef');
    if (resChefs.status === 'fulfilled' && resChefs.value.ok) {
        const chefs = await resChefs.value.json();
        selChef.innerHTML =
            '<option value="">— Seleccionar chef —</option>' +
            chefs.map(c => `<option value="${c.cedula}">${c.nombre}</option>`).join('');
    } else {
        selChef.innerHTML = '<option value="">Error al cargar chefs</option>';
        console.warn('[openAddModal] No se pudieron cargar los chefs');
    }

    // Poblar lista de ingredientes disponibles
    const selIng = document.getElementById('lst-disponibles');
    if (resIngs.status === 'fulfilled' && resIngs.value.ok) {
        const ings = await resIngs.value.json();
        selIng.innerHTML = ings
            .sort((a, b) => a.descripcion.localeCompare(b.descripcion))
            .map(i =>
                `<option value="${i.descripcion}">${i.descripcion} (Stock: ${i.cantidadStock})</option>`
            ).join('');
    } else {
        selIng.innerHTML = '<option disabled>Error al cargar ingredientes</option>';
        console.warn('[openAddModal] No se pudieron cargar los ingredientes');
    }

    document.getElementById('modal-backdrop').classList.add('open');
}

function closeAddModal() {
    if (confirm('¿Cancelar? Se perderán los datos ingresados.'))
        document.getElementById('modal-backdrop').classList.remove('open');
}

function closeOnBackdrop(e) {
    if (e.target === document.getElementById('modal-backdrop')) closeAddModal();
}

function moverIngrediente(dir) {
    const fromId = dir === 'agregar' ? 'lst-disponibles' : 'lst-seleccionados';
    const toId   = dir === 'agregar' ? 'lst-seleccionados' : 'lst-disponibles';
    const from   = document.getElementById(fromId);
    const to     = document.getElementById(toId);
    [...from.selectedOptions].forEach(opt => {
        to.appendChild(opt.cloneNode(true));
        from.removeChild(opt);
    });
    updateIngCount();
}

function updateIngCount() {
    const n = document.getElementById('lst-seleccionados').options.length;
    document.getElementById('ing-count').textContent =
        `${n} ingrediente${n !== 1 ? 's' : ''} seleccionado${n !== 1 ? 's' : ''}`;
}

/* ══════════════════════════════════════════════════════════════
   GUARDAR ALIMENTO
   POST /api/menu/{menuId}/alimento-completo
   Body: AlimentoCompletoRequest
   ══════════════════════════════════════════════════════════════ */
async function guardarAlimento() {
    const errors = [];

    const nombre      = document.getElementById('f-nombre').value.trim();
    const precio      = parseFloat(document.getElementById('f-precio').value);
    const tipo        = document.getElementById('f-tipo').value;
    const receta      = document.getElementById('f-receta').value.trim();
    const proceso     = document.getElementById('f-proceso').value.trim();
    const chefCedula  = document.getElementById('f-chef').value;
    const ingredientes = [...document.getElementById('lst-seleccionados').options]
        .map(o => o.value);

    if (!nombre)                       errors.push('El nombre del alimento es obligatorio.');
    if (isNaN(precio) || precio <= 0)  errors.push('El precio debe ser mayor a 0.');
    if (!chefCedula)                   errors.push('Debe seleccionar un chef.');
    if (!receta)                       errors.push('El nombre de la receta es obligatorio.');
    if (!proceso)                      errors.push('La descripción del proceso es obligatoria.');
    if (!ingredientes.length)          errors.push('Debe seleccionar al menos un ingrediente.');

    if (errors.length) { showModalErrors(errors); return; }

    try {
        const res = await fetch(`${API_MENU}/${MENU_ID}/alimento-completo`, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                nombreAlimento:              nombre,
                precio:                      precio,
                tipo:                        tipo,
                nombreReceta:                receta,
                descripcionProceso:          proceso,
                chefCedula:                  chefCedula,
                ingredientesDescripciones:   ingredientes,
            }),
        });

        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        document.getElementById('modal-backdrop').classList.remove('open');
        await cargarMenu();
        setStatus(`✓ "${nombre}" agregado al menú`, true);

    } catch (err) {
        console.error('[guardarAlimento]', err);
        showModalErrors(['Error al guardar. Revisá la consola para más detalles.']);
    }
}

/* ══════════════════════════════════════════════════════════════
   UTILIDADES
   ══════════════════════════════════════════════════════════════ */
function showModalErrors(errs) {
    const el = document.getElementById('modal-errors');
    el.innerHTML = errs.map(e => `• ${e}`).join('<br>');
    el.style.display = 'block';
}
function hideModalErrors() {
    const el = document.getElementById('modal-errors');
    if (el) el.style.display = 'none';
}

function setStatus(msg, ok) {
    document.getElementById('status-text').textContent = msg;
    document.getElementById('status-dot').className = 'status-dot' + (ok ? '' : ' error');
}

/* ── Init ─────────────────────────────────────────────────── */
cargarMenu();