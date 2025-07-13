
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BusBookingApp {
    static class Bus {
        int busNo, capacity;
        Bus(int busNo, int capacity){ this.busNo=busNo; this.capacity=capacity; }
    }
    static class Customer {
        String name; Customer(String name){this.name=name;}
    }
    static class Booking {
        Customer customer; int busNo; LocalDate date;
        Booking(Customer c, int busNo, LocalDate date){ this.customer = c; this.busNo = busNo; this.date = date; }
    }

    static class DataStore {
        List<Bus> buses = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();
        DataStore(){
            buses.add(new Bus(1,2));
            buses.add(new Bus(2,3));
            buses.add(new Bus(3,1));
        }
        Optional<Bus> findBus(int busNo){
            return buses.stream().filter(b->b.busNo==busNo).findFirst();
        }
        long countBooked(int busNo, LocalDate date){
            return bookings.stream().filter(b->b.busNo==busNo && b.date.equals(date)).count();
        }
        void addBooking(Booking b){ bookings.add(b); }
        void displayBuses(LocalDate date){
            System.out.println("\n--- Available Buses ---");
            for(Bus b : buses){
                long used = countBooked(b.busNo, date);
                long available = b.capacity - used;
                System.out.printf("Bus #%d | Capacity: %d | Available seats on %s: %d%n",
                    b.busNo, b.capacity, date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), available);
            }
            System.out.println("-----------------------");
        }
    }

    static class BookingService {
        DataStore store;
        BookingService(DataStore store){ this.store = store; }
        boolean isAvailable(int busNo, LocalDate date){
            Optional<Bus> ob = store.findBus(busNo);
            if(!ob.isPresent()) return false;
            return store.countBooked(busNo, date) < ob.get().capacity;
        }
        boolean book(Customer c, int busNo, LocalDate date){
            if(isAvailable(busNo, date)){
                store.addBooking(new Booking(c,busNo,date));
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DataStore store = new DataStore();
        BookingService svc = new BookingService(store);

        System.out.println("=== Welcome to Bus Booking System ===");
        while(true) {
            store.displayBuses(LocalDate.now());
            System.out.println("\nMenu:");
            System.out.println("1. Book a seat");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            String opt = sc.nextLine().trim();
            if(opt.equals("2")) break;
            if(!opt.equals("1")) {
                System.out.println("Invalid option. Please try again.");
                continue;
            }

            System.out.print("Enter your name: ");
            String name = sc.nextLine().trim();
            if(name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                continue;
            }

            System.out.print("Enter bus number: ");
            int busNo;
            try { busNo = Integer.parseInt(sc.nextLine().trim()); }
            catch(NumberFormatException e){ System.out.println("Invalid bus number."); continue; }

            if(!store.findBus(busNo).isPresent()) {
                System.out.println("Bus number does not exist.");
                continue;
            }

            System.out.print("Enter date (dd-MM-yyyy): ");
            LocalDate date;
            try {
                date = LocalDate.parse(sc.nextLine().trim(), df);
            } catch(Exception e) {
                System.out.println("Invalid date format.");
                continue;
            }

            if(svc.book(new Customer(name), busNo, date)) {
                System.out.printf("Booking confirmed for %s on bus #%d at %s%n",
                    name, busNo, date.format(df));
            } else {
                System.out.println("Sorry, no seats available for that bus on the selected date.");
            }
        }

        System.out.println("\nThank you for using Bus Booking System. Goodbye!");
        sc.close();
    }
}
