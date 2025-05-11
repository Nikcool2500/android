package ru.mirea.chirka.employeedb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText, superpowerEditText, strengthEditText;
    private TextView resultTextView;
    private AppDatabase db;
    private EmployeeDao employeeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        superpowerEditText = findViewById(R.id.superpowerEditText);
        strengthEditText = findViewById(R.id.strengthEditText);
        resultTextView = findViewById(R.id.resultTextView);
        Button addButton = findViewById(R.id.addButton);
        Button listButton = findViewById(R.id.listButton);

        db = App.getInstance().getDatabase();
        employeeDao = db.employeeDao();

        addButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String superpower = superpowerEditText.getText().toString();
            int strength = Integer.parseInt(strengthEditText.getText().toString());

            Employee employee = new Employee(name, superpower, strength);
            employeeDao.insert(employee);
            Toast.makeText(this, "Супергерой добавлен", Toast.LENGTH_SHORT).show();
        });

        listButton.setOnClickListener(v -> {
            List<Employee> employees = employeeDao.getAll();
            StringBuilder sb = new StringBuilder();
            for (Employee emp : employees) {
                sb.append("ID: ").append(emp.id)
                        .append(", Имя: ").append(emp.name)
                        .append(", Способность: ").append(emp.superpower)
                        .append(", Сила: ").append(emp.strength)
                        .append("\n\n");
            }
            resultTextView.setText(sb.toString());
        });
    }
}