# Отчёт по проекту SkillFlow

## 1. Описание реализованного проекта

**SkillFlow** - веб-приложение для управления онлайн-обучением, разработанное на Spring Boot. Реализованы полные CRUD операции для трёх основных сущностей: эксперты, студенты и курсы.

### Реализованные функции:
- ✅ Просмотр списков экспертов, студентов, курсов
- ✅ Добавление новых записей  
- ✅ Редактирование существующих записей
- ✅ Удаление записей с подтверждением
- ✅ Валидация вводимых данных на стороне сервера и клиента
- ✅ Связи между таблицами (эксперты-курсы)

## 2. Реализованные Java классы и методы

### 2.1. Главный класс приложения

**SkillFlowApplication.java**
```java
package com.skillflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Spring Boot приложения
 * Запускает веб-приложение SkillFlow на порту 8080
 */
@SpringBootApplication
public class SkillFlowApplication {
    public static void main(String[] args) {
        // Запуск Spring Boot приложения
        SpringApplication.run(SkillFlowApplication.class, args);
    }
}
```

### 2.2. Entity классы (Сущности базы данных)

**Expert.java** - сущность эксперта/преподавателя
```java
@Entity
@Table(name = "experts")
public class Expert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_эксперта")
    private Long id;  // Уникальный идентификатор эксперта

    @NotBlank(message = "Имя обязательно")
    @Column(name = "имя_эксперта")
    private String firstName;  // Имя эксперта

    @NotBlank(message = "Фамилия обязательна")
    @Column(name = "фамилия_эксперта")
    private String lastName;   // Фамилия эксперта

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    @Column(name = "email_эксперта")
    private String email;      // Email адрес

    @Column(name = "биография_эксперта")
    private String biography;  // Профессиональная биография

    @NotBlank(message = "Специализация обязательна")
    @Column(name = "специализация_эксперта")
    private String specialization;  // Специализация эксперта

    // Связь один-ко-многим с курсами - один эксперт может вести много курсов
    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
    private List<Course> courses;

    // Конструктор по умолчанию (требуется JPA)
    public Expert() {}

    // Конструктор с параметрами для удобного создания объектов
    public Expert(String firstName, String lastName, String email, String biography, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.biography = biography;
        this.specialization = specialization;
    }

    // Геттеры и сеттеры для доступа к полям
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    // ... остальные геттеры и сеттеры
}
```

**Student.java** - сущность студента
```java
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_студента")
    private Long id;  // Уникальный идентификатор студента

    @NotBlank(message = "Имя обязательно")
    @Column(name = "имя_студента")
    private String firstName;  // Имя студента

    @NotBlank(message = "Фамилия обязательна")
    @Column(name = "фамилия_студента")
    private String lastName;   // Фамилия студента

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    @Column(name = "email_студента")
    private String email;      // Email студента

    @Column(name = "дата_регистрации_студента")
    private LocalDateTime registrationDate;  // Дата регистрации на платформе

    // Связь один-ко-многим с учебными планами
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudyPlan> studyPlans;

    public Student() {}

    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = LocalDateTime.now();  // Автоматическая установка даты регистрации
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... остальные геттеры и сеттеры
}
```

**Course.java** - сущность курса
```java
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_курса")
    private Long id;  // Уникальный идентификатор курса

    @NotBlank(message = "Название курса обязательно")
    @Column(name = "название_курса")
    private String title;  // Название курса

    @Column(name = "описание_курса")
    private String description;  // Описание курса

    @Column(name = "дата_создания_курса")
    private LocalDateTime creationDate;  // Дата создания курса

    // Связь многие-к-одному с экспертом - курс принадлежит одному эксперту
    @ManyToOne
    @JoinColumn(name = "id_эксперта")
    private Expert expert;  // Эксперт, создавший курс

    // Связь один-ко-многим с ценами курса
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CoursePrice> prices;

    // Связь один-ко-многим с учебными планами
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<StudyPlan> studyPlans;

    public Course() {}

    public Course(String title, String description, Expert expert) {
        this.title = title;
        this.description = description;
        this.expert = expert;
        this.creationDate = LocalDateTime.now();  // Автоматическая установка даты создания
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... остальные геттеры и сеттеры
}
```

**CoursePrice.java** - сущность цены курса
```java
@Entity
@Table(name = "course_prices")
public class CoursePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Уникальный идентификатор цены

    // Связь многие-к-одному с курсом
    @ManyToOne
    @JoinColumn(name = "id_курса")
    @NotNull(message = "Курс обязателен")
    private Course course;  // Курс, к которому относится цена

    @Column(name = "дата_начала_действия_цены")
    @NotNull(message = "Дата начала действия обязательна")
    private LocalDateTime startDate;  // Дата начала действия цены

    @Column(name = "цена_курса")
    @DecimalMin(value = "0.0", message = "Цена должна быть положительной")
    private BigDecimal price;  // Стоимость курса

    @Column(name = "дата_окончания_действия_цены")
    private LocalDateTime endDate;  // Дата окончания действия цены

    public CoursePrice() {}

    public CoursePrice(Course course, LocalDateTime startDate, BigDecimal price) {
        this.course = course;
        this.startDate = startDate;
        this.price = price;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... остальные геттеры и сеттеры
}
```

**StudyPlan.java** - сущность учебного плана
```java
@Entity
@Table(name = "study_plans")
public class StudyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_плана")
    private Long id;  // Уникальный идентификатор учебного плана

    // Связь многие-к-одному со студентом
    @ManyToOne
    @JoinColumn(name = "id_студента")
    @NotNull(message = "Студент обязателен")
    private Student student;  // Студент учебного плана

    // Связь многие-к-одному с курсом
    @ManyToOne
    @JoinColumn(name = "id_курса")
    @NotNull(message = "Курс обязателен")
    private Course course;    // Курс учебного плана

    @Column(name = "дата_начала")
    private LocalDateTime startDate;  // Дата начала обучения

    @Column(name = "плановая_дата_окончания")
    private LocalDateTime plannedEndDate;  // Плановая дата окончания

    @Column(name = "статус_плана")
    private String status;    // Статус учебного плана

    public StudyPlan() {}

    public StudyPlan(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.startDate = LocalDateTime.now();  // Автоматическая установка даты начала
        this.status = "активен";  // Статус по умолчанию
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... остальные геттеры и сеттеры
}
```

### 2.3. Repository интерфейсы (Доступ к данным)

**ExpertRepository.java**
```java
@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {
    // Найти экспертов по специализации
    List<Expert> findBySpecialization(String specialization);
    
    // Найти экспертов по фамилии (без учета регистра)
    List<Expert> findByLastNameContainingIgnoreCase(String lastName);
}
```

**StudentRepository.java**
```java
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // Найти студентов по фамилии (без учета регистра)
    List<Student> findByLastNameContainingIgnoreCase(String lastName);
    
    // Найти студента по email
    Student findByEmail(String email);
}
```

**CourseRepository.java**
```java
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Найти курсы по названию (без учета регистра)
    List<Course> findByTitleContainingIgnoreCase(String title);
    
    // Найти курсы по ID эксперта
    List<Course> findByExpertId(Long expertId);
}
```

**CoursePriceRepository.java**
```java
@Repository
public interface CoursePriceRepository extends JpaRepository<CoursePrice, Long> {
    // Найти цены по ID курса
    List<CoursePrice> findByCourseId(Long courseId);
}
```

**StudyPlanRepository.java**
```java
@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
    // Найти учебные планы по ID студента
    List<StudyPlan> findByStudentId(Long studentId);
    
    // Найти учебные планы по ID курса
    List<StudyPlan> findByCourseId(Long courseId);
    
    // Найти учебные планы по статусу
    List<StudyPlan> findByStatus(String status);
}
```

### 2.4. Service классы (Бизнес-логика)

**ExpertService.java**
```java
@Service
public class ExpertService {
    @Autowired
    private ExpertRepository expertRepository;

    // Получить всех экспертов
    public List<Expert> getAllExperts() {
        return expertRepository.findAll();
    }

    // Получить эксперта по ID
    public Optional<Expert> getExpertById(Long id) {
        return expertRepository.findById(id);
    }

    // Сохранить эксперта (создание или обновление)
    public Expert saveExpert(Expert expert) {
        return expertRepository.save(expert);
    }

    // Удалить эксперта по ID
    public void deleteExpert(Long id) {
        expertRepository.deleteById(id);
    }

    // Получить экспертов по специализации
    public List<Expert> getExpertsBySpecialization(String specialization) {
        return expertRepository.findBySpecialization(specialization);
    }
}
```

**StudentService.java**
```java
@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    // Получить всех студентов
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Получить студента по ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Сохранить студента (создание или обновление)
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Удалить студента по ID
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
```

**CourseService.java**
```java
@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    // Получить все курсы
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Получить курс по ID
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // Сохранить курс (создание или обновление)
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    // Удалить курс по ID
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    // Получить курсы по ID эксперта
    public List<Course> getCoursesByExpert(Long expertId) {
        return courseRepository.findByExpertId(expertId);
    }
}
```

### 2.5. Controller классы (Веб-слой)

**MainController.java** - контроллер главной страницы
```java
@Controller
public class MainController {
    // Обработка GET запроса на главную страницу
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "SkillFlow - Система управления обучением");
        return "index";  // Возвращает шаблон index.html
    }
}
```

**ExpertController.java** - CRUD операции для экспертов
```java
@Controller
@RequestMapping("/experts")  // Базовый URL для всех методов контроллера
public class ExpertController {
    @Autowired
    private ExpertService expertService;

    // GET /experts - отображение списка всех экспертов
    @GetMapping
    public String getAllExperts(Model model) {
        model.addAttribute("experts", expertService.getAllExperts());
        return "experts/list";  // Шаблон списка экспертов
    }

    // GET /experts/{id} - просмотр конкретного эксперта
    @GetMapping("/{id}")
    public String getExpertById(@PathVariable Long id, Model model) {
        Optional<Expert> expert = expertService.getExpertById(id);
        if (expert.isPresent()) {
            model.addAttribute("expert", expert.get());
            return "experts/view";  // Шаблон просмотра эксперта
        }
        return "redirect:/experts";  // Перенаправление если эксперт не найден
    }

    // GET /experts/new - форма создания нового эксперта
    @GetMapping("/new")
    public String showExpertForm(Model model) {
        model.addAttribute("expert", new Expert());
        return "experts/form";  // Шаблон формы эксперта
    }

    // POST /experts - сохранение нового или обновленного эксперта
    @PostMapping
    public String saveExpert(@Valid @ModelAttribute Expert expert, BindingResult result) {
        // Проверка ошибок валидации
        if (result.hasErrors()) {
            return "experts/form";  // Возврат к форме при ошибках
        }
        expertService.saveExpert(expert);
        return "redirect:/experts";  // Перенаправление после успешного сохранения
    }

    // GET /experts/{id}/edit - форма редактирования эксперта
    @GetMapping("/{id}/edit")
    public String editExpert(@PathVariable Long id, Model model) {
        Optional<Expert> expert = expertService.getExpertById(id);
        if (expert.isPresent()) {
            model.addAttribute("expert", expert.get());
            return "experts/form";  // Используется та же форма что и для создания
        }
        return "redirect:/experts";
    }

    // GET /experts/{id}/delete - удаление эксперта
    @GetMapping("/{id}/delete")
    public String deleteExpert(@PathVariable Long id) {
        expertService.deleteExpert(id);
        return "redirect:/experts";  // Перенаправление после удаления
    }
}
```

**StudentController.java** - CRUD операции для студентов (аналогично ExpertController)

**CourseController.java** - CRUD операции для курсов с привязкой к экспертам
```java
@Controller
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private ExpertService expertService;

    // GET /courses - список всех курсов
    @GetMapping
    public String getAllCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses/list";
    }

    // GET /courses/{id} - просмотр конкретного курса
    @GetMapping("/{id}")
    public String getCourseById(@PathVariable Long id, Model model) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            return "courses/view";
        }
        return "redirect:/courses";
    }

    // GET /courses/new - форма создания курса
    @GetMapping("/new")
    public String showCourseForm(Model model) {
        model.addAttribute("course", new Course());
        // Получение списка экспертов для выпадающего списка
        List<Expert> experts = expertService.getAllExperts();
        model.addAttribute("experts", experts);
        return "courses/form";
    }

    // POST /courses - сохранение курса
    @PostMapping
    public String saveCourse(@Valid @ModelAttribute Course course,
            BindingResult result,
            @RequestParam Long expertId,  // ID эксперта из формы
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("experts", expertService.getAllExperts());
            return "courses/form";
        }

        // Поиск эксперта по ID и привязка к курсу
        Optional<Expert> expert = expertService.getExpertById(expertId);
        if (expert.isPresent()) {
            course.setExpert(expert.get());
            courseService.saveCourse(course);
            return "redirect:/courses";
        } else {
            model.addAttribute("error", "Эксперт не найден");
            model.addAttribute("experts", expertService.getAllExperts());
            return "courses/form";
        }
    }

    // GET /courses/{id}/edit - форма редактирования курса
    @GetMapping("/{id}/edit")
    public String editCourse(@PathVariable Long id, Model model) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            model.addAttribute("experts", expertService.getAllExperts());
            return "courses/form";
        }
        return "redirect:/courses";
    }

    // GET /courses/{id}/delete - удаление курса
    @GetMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}
```

## 3. Конфигурационные файлы

**application.properties** - настройки Spring Boot приложения
```properties
# Настройки базы данных PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/skillflow
spring.datasource.username=postgres
spring.datasource.password=5432

# Настройки JPA и Hibernate
spring.jpa.hibernate.ddl-auto=update        # Автоматическое обновление схемы БД
spring.jpa.show-sql=true                    # Показывать SQL запросы в логах
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Настройки сервера
server.port=8080                            # Порт приложения

# Настройки Thymeleaf шаблонов
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML
spring.thymeleaf.encoding=UTF-8
```

**pom.xml** - зависимости Maven
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.skillflow</groupId>
    <artifactId>skillflow</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.0</version>
    </parent>

    <dependencies>
        <!-- Spring Boot Web для создания веб-приложения -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- Thymeleaf для шаблонов HTML -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        
        <!-- Spring Data JPA для работы с базой данных -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <!-- Драйвер PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
        
        <!-- Валидация данных -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        
        <!-- Тестирование -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

## 4. HTML шаблоны (реализованные)

- **index.html** - главная страница с навигацией
- **experts/list.html** - список всех экспертов
- **experts/form.html** - форма создания/редактирования эксперта  
- **experts/view.html** - просмотр деталей эксперта
- **students/list.html** - список всех студентов
- **students/form.html** - форма создания/редактирования студента
- **students/view.html** - просмотр деталей студента
- **courses/list.html** - список всех курсов
- **courses/form.html** - форма создания/редактирования курса
- **courses/view.html** - просмотр деталей курса

## 5. Инструкция по запуску

### Требования:
- Java JDK 8+
- Maven 3.6+
- PostgreSQL 12+

**Доступ к приложению:** http://localhost:8080

### Основные URL:
- `/` - главная страница
- `/experts` - управление экспертами
- `/students` - управление студентами
- `/courses` - управление курсами
