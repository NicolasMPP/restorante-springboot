package com.nicolasperez.restorantespringboot.config;

import com.nicolasperez.restorantespringboot.entities.*;
import com.nicolasperez.restorantespringboot.repositories.*;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@Transactional
public class InicializadorBD implements ApplicationRunner {

    // ============================================================
    // REPOSITORIES
    // ============================================================

    private final PersonaRepository personaRepository;
    private final ChefRepository chefRepository;
    private final GerenteRepository gerenteRepository;
    private final MeseroRepository meseroRepository;
    private final ClienteRepository clienteRepository;
    private final IngredienteRepository ingredienteRepository;
    private final RecetaRepository recetaRepository;
    private final AlimentoRepository alimentoRepository;
    private final MenuRepository menuRepository;
    private final DespensaRepository despensaRepository;

    public InicializadorBD(
            PersonaRepository personaRepository,
            ChefRepository chefRepository,
            GerenteRepository gerenteRepository,
            MeseroRepository meseroRepository,
            ClienteRepository clienteRepository,
            IngredienteRepository ingredienteRepository,
            RecetaRepository recetaRepository,
            AlimentoRepository alimentoRepository,
            MenuRepository menuRepository,
            DespensaRepository despensaRepository
    ) {
        this.personaRepository = personaRepository;
        this.chefRepository = chefRepository;
        this.gerenteRepository = gerenteRepository;
        this.meseroRepository = meseroRepository;
        this.clienteRepository = clienteRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.recetaRepository = recetaRepository;
        this.alimentoRepository = alimentoRepository;
        this.menuRepository = menuRepository;
        this.despensaRepository = despensaRepository;
    }

    // ============================================================
    // APPLICATION RUNNER
    // ============================================================

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("\n" + "=".repeat(60));
        System.out.println("🗄️  INICIALIZANDO BASE DE DATOS");
        System.out.println("=".repeat(60));

        if (yaInicializada()) {
            System.out.println("✓ La base de datos ya tiene datos. Omitiendo carga inicial.");
            System.out.println("=".repeat(60) + "\n");
            return;
        }

        System.out.println("📦 Base de datos vacía. Insertando datos de prueba...");

        try {

            // ====================================================
            // PERSONAS
            // ====================================================

            Gerente gerente = insertarGerente();

            Chef chefCarina = insertarChef2();
            Chef chefAndres = insertarChef1();

            insertarMesero();
            insertarCliente();

            // ====================================================
            // INGREDIENTES
            // ====================================================

            Ingrediente[] ingredientes = insertarIngredientes();

            // ====================================================
            // RECETAS
            // ====================================================

            Receta[] recetas = insertarRecetas(chefCarina, chefAndres);

            asociarIngredientesARecetas(recetas, ingredientes);

            // ====================================================
            // ALIMENTOS
            // ====================================================

            Alimento[] alimentos = insertarAlimentos(recetas);

            // ====================================================
            // MENU
            // ====================================================

            Menu menu = insertarMenu(gerente);

            asociarAlimentosAMenu(menu, alimentos);

            // ====================================================
            // DESPENSA
            // ====================================================

            Despensa despensa = insertarDespensa(gerente);

            asociarIngredientesADespensa(despensa, ingredientes);

            System.out.println("✅ Base de datos inicializada correctamente.");

        } catch (Exception e) {
            System.err.println("✗ Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=".repeat(60) + "\n");
    }

    // ============================================================
    // VERIFICACION
    // ============================================================

    private boolean yaInicializada() {
        return personaRepository.count() > 0;
    }

    // ============================================================
    // PERSONAS
    // ============================================================

    private Gerente insertarGerente() throws Exception {

        Gerente gerente = new Gerente(
                "Martín Vargas",
                "1234567890",
                "2615488978",
                "martinchef@itu.masterchef.com",
                "MChef55",
                "Poo1234"
        );

        return gerenteRepository.save(gerente);
    }

    private Chef insertarChef1() throws Exception {

        Chef chef = new Chef();

        chef.setNombre("Andrés Martínez");
        chef.setCedula("2345678901");
        chef.setTelefono("555-0002");
        chef.setCorreo("andyChef@itu.masterchef.com");
        chef.setUsuario("AndresMagno");
        chef.setContrasenia("4567Andrecito");
        chef.setSalario(new BigDecimal("2500.00"));
        chef.setFechaVinculacion(parsearFecha("2023-01-15"));
        chef.setHoraIngreso(parsearHora("08:00:00"));
        chef.setHoraSalida(parsearHora("16:00:00"));

        return chefRepository.save(chef);
    }

    private Chef insertarChef2() throws Exception {

        Chef chef = new Chef();

        chef.setNombre("Carina Sosa");
        chef.setCedula("3456789012");
        chef.setTelefono("555-0003");
        chef.setCorreo("carinaChef@itu.masterchef.com");
        chef.setUsuario("CarinaSosa");
        chef.setContrasenia("2020SosaCarina");
        chef.setSalario(new BigDecimal("2800.00"));
        chef.setFechaVinculacion(parsearFecha("2023-02-20"));
        chef.setHoraIngreso(parsearHora("08:00:00"));
        chef.setHoraSalida(parsearHora("16:00:00"));

        return chefRepository.save(chef);
    }

    private void insertarMesero() throws Exception {

        Mesero mesero = new Mesero();

        mesero.setNombre("Ana Martínez");
        mesero.setCedula("4567890123");
        mesero.setTelefono("555-0004");
        mesero.setCorreo("ana@restaurante.com");
        mesero.setUsuario("mesero1");
        mesero.setContrasenia("mesero123");
        mesero.setSalario(new BigDecimal("1800.00"));
        mesero.setFechaVinculacion(parsearFecha("2023-03-10"));
        mesero.setHoraIngreso(parsearHora("10:00:00"));
        mesero.setHoraSalida(parsearHora("18:00:00"));

        meseroRepository.save(mesero);
    }

    private void insertarCliente() throws Exception {

        Cliente cliente = new Cliente(
                "Pedro Sánchez",
                "5678901234",
                "555-0005",
                "pedro@email.com"
        );

        clienteRepository.save(cliente);
    }

    // ============================================================
    // INGREDIENTES
    // ============================================================

    private Ingrediente[] insertarIngredientes() throws Exception {

        Object[][] datos = {
                {"Tomate", 100},
                {"Cebolla", 80},
                {"Ajo", 50},
                {"Pasta", 120},
                {"Queso Parmesano", 40},
                {"Aceite de Oliva", 60},
                {"Sal", 200},
                {"Pimienta", 150},
                {"Carne de Res", 45},
                {"Pollo", 70},
                {"Huevos", 60},
                {"Tocino", 30},
                {"Albahaca", 25},
                {"Mozzarella", 35},
                {"Mantequilla", 50},
                {"Carne Vacuna", 55}
        };

        Ingrediente[] ingredientes = new Ingrediente[datos.length];

        for (int i = 0; i < datos.length; i++) {

            Ingrediente ingrediente = new Ingrediente(
                    (String) datos[i][0],
                    (Integer) datos[i][1]
            );

            ingredientes[i] = ingredienteRepository.save(ingrediente);

            System.out.println("  + Ingrediente: " + ingrediente.getDescripcion());
        }

        return ingredientes;
    }

    // ============================================================
    // RECETAS
    // ============================================================

    private Receta[] insertarRecetas(Chef carina, Chef andres) throws Exception {

        Receta[] recetas = new Receta[4];

        recetas[0] = recetaRepository.save(
                new Receta(
                        "Pasta Carbonara",
                        "Cocinar la pasta al dente en agua con sal. Freír el tocino hasta que esté crujiente. Batir los huevos con el queso parmesano. Mezclar todo fuera del fuego.",
                        carina
                )
        );

        recetas[1] = recetaRepository.save(
                new Receta(
                        "Bistec a la Parrilla",
                        "Sazonar la carne con sal y pimienta. Calentar la parrilla a fuego alto. Cocinar 4-5 minutos por lado. Dejar reposar 5 minutos.",
                        andres
                )
        );

        recetas[2] = recetaRepository.save(
                new Receta(
                        "Ensalada Caprese",
                        "Cortar tomates y mozzarella en rodajas. Intercalar en un plato con hojas de albahaca. Rociar con aceite de oliva.",
                        carina
                )
        );

        recetas[3] = recetaRepository.save(
                new Receta(
                        "Pollo al Horno",
                        "Marinar el pollo con ajo y especias. Hornear a 180°C durante 45 minutos hasta que esté dorado.",
                        andres
                )
        );

        for (Receta receta : recetas) {
            System.out.println("  + Receta: " + receta.getNombreReceta());
        }

        return recetas;
    }

    // ============================================================
    // RELACIONES RECETA - INGREDIENTES
    // ============================================================

    private void asociarIngredientesARecetas(
            Receta[] recetas,
            Ingrediente[] ingredientes
    ) {

        int[] ingCarbonaraIdx = {3, 4, 10, 11, 6, 7};
        int[] ingBistecIdx = {8, 2, 5, 6, 7};
        int[] ingCapreseIdx = {0, 13, 12, 5, 6};
        int[] ingPolloIdx = {9, 2, 5, 6, 7};

        int[][] grupos = {
                ingCarbonaraIdx,
                ingBistecIdx,
                ingCapreseIdx,
                ingPolloIdx
        };

        for (int r = 0; r < recetas.length; r++) {

            Receta receta = recetaRepository
                    .findById(recetas[r].getId())
                    .orElseThrow();

            for (int idx : grupos[r]) {

                Ingrediente ingrediente = ingredienteRepository
                        .findById(ingredientes[idx].getId())
                        .orElseThrow();

                receta.agregarIngrediente(ingrediente);
            }

            recetaRepository.save(receta);
        }

        System.out.println("  + Ingredientes asociados a recetas");
    }

    // ============================================================
    // ALIMENTOS
    // ============================================================

    private Alimento[] insertarAlimentos(Receta[] recetas) throws Exception {

        Object[][] datos = {

                {"Pasta Carbonara Premium", 18.50, 0, "PLATO_FUERTE"},
                {"Bistec Angus 300g", 25.00, 1, "PLATO_FUERTE"},
                {"Ensalada Caprese", 12.00, 2, "ADICIONAL"},
                {"Pollo al Horno con Hierbas", 22.00, 3, "PLATO_FUERTE"},
                {"Tiramisú Casero", 8.50, -1, "POSTRE"},
                {"Helado de Chocolate", 6.00, -1, "POSTRE"},
                {"Coca-Cola", 3.50, -1, "BEBIDA"},
                {"Agua Mineral", 2.50, -1, "BEBIDA"},
                {"Jugo Natural de Naranja", 4.50, -1, "BEBIDA"},
                {"Pan de Ajo", 4.00, -1, "ADICIONAL"}
        };

        Alimento[] alimentos = new Alimento[datos.length];

        for (int i = 0; i < datos.length; i++) {

            String nombre = (String) datos[i][0];
            BigDecimal precio = BigDecimal.valueOf((Double) datos[i][1]);
            int recetaIdx = (int) datos[i][2];
            String tipo = (String) datos[i][3];

            Receta receta = (recetaIdx >= 0)
                    ? recetas[recetaIdx]
                    : null;

            Alimento alimento = crearAlimentoPorTipo(
                    nombre,
                    precio,
                    receta,
                    tipo
            );

            alimentos[i] = alimentoRepository.save(alimento);

            System.out.println("  + Alimento: " + nombre);
        }

        return alimentos;
    }

    private Alimento crearAlimentoPorTipo(
            String nombre,
            BigDecimal precio,
            Receta receta,
            String tipo
    ) {

        switch (tipo) {

            case "PLATO_FUERTE":
                return new PlatoFuerte(nombre, precio, receta);

            case "POSTRE":
                return new Postres(nombre, precio, receta);

            case "BEBIDA":
                return new Bebida(nombre, precio, receta);

            case "ADICIONAL":
                return new Adicionales(nombre, precio, receta);

            default:
                return new Alimento(nombre, precio, receta);
        }
    }

    // ============================================================
    // MENU
    // ============================================================

    private Menu insertarMenu(Gerente gerente) throws Exception {

        Menu menu = new Menu(
                "Menú Principal del Día",
                gerente
        );

        menu = menuRepository.save(menu);

        System.out.println("  + Menú: " + menu.getNombreMenu());

        return menu;
    }

    private void asociarAlimentosAMenu(
            Menu menu,
            Alimento[] alimentos
    ) {

        Menu menuManaged = menuRepository
                .findById(menu.getId())
                .orElseThrow();

        for (Alimento alimento : alimentos) {
            menuManaged.agregarAlimento(alimento);
        }

        menuRepository.save(menuManaged);

        System.out.println("  + " + alimentos.length + " alimentos asociados al menú");
    }

    // ============================================================
    // DESPENSA
    // ============================================================

    private Despensa insertarDespensa(Gerente gerente) throws Exception {

        Despensa despensa = new Despensa(gerente);

        despensa = despensaRepository.save(despensa);

        System.out.println("  + Despensa creada para gerente: " + gerente.getNombre());

        return despensa;
    }

    private void asociarIngredientesADespensa(
            Despensa despensa,
            Ingrediente[] ingredientes
    ) {

        Despensa despensaManaged = despensaRepository
                .findById(despensa.getId())
                .orElseThrow();

        for (Ingrediente ingrediente : ingredientes) {
            despensaManaged.agregarIngrediente(ingrediente);
        }

        despensaRepository.save(despensaManaged);

        System.out.println("  + " + ingredientes.length + " ingredientes asociados a la despensa");
    }

    // ============================================================
    // UTILIDADES
    // ============================================================

    private LocalDate parsearFecha(String fecha) {

        try {
            return LocalDate.parse(fecha);
        } catch (Exception e) {
            return LocalDate.now();
        }
    }

    private LocalDateTime parsearHora(String hora) {

        try {
            return LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.parse(hora)
            );
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}