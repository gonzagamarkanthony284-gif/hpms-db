package hpms.ui.panels;

import hpms.model.*;
import hpms.util.DataStore;
import hpms.ui.components.RoundedCard;
import hpms.ui.components.SectionHeader;
import hpms.ui.components.Theme;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DashboardPanel extends JPanel {
    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);
        add(SectionHeader.info("Dashboard", "Overview: appointments, admissions, billing summaries."), BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 20, 20));
        grid.setBackground(Theme.BACKGROUND);
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(grid, BorderLayout.CENTER);

        grid.add(createCard("Patients", String.valueOf(DataStore.patients.size())));
        long todaysAppts = DataStore.appointments.values().stream().filter(a -> a.dateTime.toLocalDate().equals(LocalDate.now())).count();
        grid.add(createCard("Appointments Today", String.valueOf(todaysAppts)));
        long occupied = DataStore.rooms.values().stream().filter(r -> r.status == RoomStatus.OCCUPIED).count();
        long total = DataStore.rooms.size();
        grid.add(createCard("Bed Occupancy", occupied + "/" + total));
        long pending = DataStore.bills.values().stream().filter(b -> !b.paid).count();
        grid.add(createCard("Pending Bills", String.valueOf(pending)));
    }

    private JComponent createCard(String title, String value) {
        RoundedCard card = new RoundedCard(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Theme.FOREGROUND);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        valueLabel.setForeground(Theme.PRIMARY);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }
}
