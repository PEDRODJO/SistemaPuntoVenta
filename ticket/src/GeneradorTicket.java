/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; // Importamos la clase Scanner

/**
 *
 * @author Thinkpad E14
 */
public class GeneradorTicket {
    
    public static void main(String[] args) {
        
        // Usamos try-with-resources para asegurar que el Scanner se cierre automáticamente
        try (Scanner scanner = new Scanner(System.in)) {
            List<Producto> productos = new ArrayList<>();
            
            System.out.println("--- Sistema de Venta ---");
            
            // Bucle para pedir productos al usuario
            while (true) {
                System.out.print("Ingrese el nombre del producto (o escriba 'fin' para terminar): ");
                String nombre = scanner.nextLine();
                
                // Si el usuario escribe 'fin', salimos del bucle
                if (nombre.equalsIgnoreCase("fin")) {
                    break;
                }
                
                System.out.print("Ingrese el precio del producto: ");
                double precio = scanner.nextDouble();
                
                System.out.print("Ingrese la cantidad: ");
                int cantidad = scanner.nextInt();
                
                // IMPORTANTE: Limpiar el buffer del scanner después de leer un número
                scanner.nextLine(); 
                
                // Agregamos el nuevo producto a la lista
                productos.add(new Producto(nombre, precio, cantidad));
                System.out.println("✅ Producto agregado.");
                System.out.println("--------------------");
            }
            
            // Si no se agregaron productos, no se genera el ticket
            if (productos.isEmpty()) {
                System.out.println("No se agregaron productos. No se generará ningún ticket.");
                return; // Termina el programa
            }

            // --- A PARTIR DE AQUÍ, EL CÓDIGO ES IGUAL AL ANTERIOR ---
            
            // --- 2. CONSTRUCCIÓN DEL TICKET ---
            StringBuilder ticketBuilder = new StringBuilder();
            
            ticketBuilder.append("****************************************\n");
            ticketBuilder.append("          TIENDA 'MI NEGOCIO'           \n");
            ticketBuilder.append("       Av. Siempre Viva 742, CDMX       \n");
            ticketBuilder.append("****************************************\n");
            
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            ticketBuilder.append("Fecha: ").append(ahora.format(formatter)).append("\n");
            ticketBuilder.append("Ticket No: 001-2025\n");
            ticketBuilder.append("----------------------------------------\n");
            
            ticketBuilder.append(String.format("%-20s %5s %10s\n", "Producto", "Cant.", "Total"));
            ticketBuilder.append("----------------------------------------\n");
            
            double subtotal = 0.0;
            for (Producto p : productos) {
                ticketBuilder.append(String.format("%-20.20s %5d %10.2f\n", 
                                                   p.getNombre(), 
                                                   p.getCantidad(), 
                                                   p.getTotal()));
                subtotal += p.getTotal();
            }
            
            ticketBuilder.append("----------------------------------------\n");
            double iva = subtotal * 0.16;
            double total = subtotal + iva;
            
            ticketBuilder.append(String.format("%-25s %10.2f\n", "SUBTOTAL:", subtotal));
            ticketBuilder.append(String.format("%-25s %10.2f\n", "IVA (16%):", iva));
            ticketBuilder.append(String.format("%-25s %10.2f\n", "TOTAL:", total));
            ticketBuilder.append("****************************************\n");
            ticketBuilder.append("     GRACIAS POR SU COMPRA!      \n");
            ticketBuilder.append("****************************************\n");
            
            // --- 3. MOSTRAR Y GUARDAR EL TICKET ---
            System.out.println("\n--- TICKET GENERADO ---");
            System.out.println(ticketBuilder.toString());
            
            try (FileWriter writer = new FileWriter("ticket.txt")) {
                writer.write(ticketBuilder.toString());
                System.out.println("\n✅ Ticket guardado exitosamente como 'ticket.txt'");
            } catch (IOException e) {
                System.err.println("\n❌ Error al guardar el ticket: " + e.getMessage());
            }
        } // El scanner se cierra aquí automáticamente
    }
}

