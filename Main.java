public class Main {
    public static void main(String[] args) {

        // Console Testing (Polymorphism)
        Employee e1 = new FullTimeEmployee("Mayank", 101, 40000, 10000);
        Employee e2 = new PartTimeEmployee("Rahul", 102, 80, 150);

        e1.displayDetails(e1.calculateSalary());
        e2.displayDetails(e2.calculateSalary());

        // Launch UI
        new PayrollUI();
    }
}