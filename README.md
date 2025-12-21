# Отчёт по проекту SkillFlow

## 1. Описание реализованного проекта

**SkillFlow** - веб-приложение для управления онлайн-обучением, разработанное на Spring Boot с использованием полного стека технологий. Система предоставляет комплексное решение для управления образовательным процессом: от экспертов и студентов до курсов и учебных планов.

### Реализованные функции:
- ✅ Полный CRUD для экспертов, студентов, курсов и учебных планов
- ✅ Связи между сущностями (эксперты-курсы, студенты-учебные планы, курсы-учебные планы)
- ✅ Валидация данных на стороне сервера (Bean Validation) и клиента (HTML5 validation)
- ✅ Пагинация и фильтрация данных
- ✅ Анимации и интерактивный интерфейс
- ✅ Обработка ошибок и валидационных исключений
- ✅ Локализация дат и времени
- ✅ Статистика и аналитика по каждому модулю

## 2. Архитектура проекта

### 2.1. Многослойная архитектура
```
Презентационный слой (Controllers + Thymeleaf)
       ↓
Бизнес-логика (Services)
       ↓
Доступ к данным (Repositories + JPA)
       ↓
База данных (PostgreSQL)
```

### 2.2. Сущности базы данных и их связи
```
Expert (1) ----- (n) Course (1) ----- (n) StudyPlan (n) ----- (1) Student
                    |                           ↑
                    └── (n) CoursePrice         └── Статусы: активен, завершен, приостановлен, отменен
```

## 3. Реализованные Java классы и методы

### 3.1. Главный класс приложения

**SkillFlowApplication.java** - точка входа в Spring Boot приложение
```java
/**
 * Главный класс приложения SkillFlow - точка входа в Spring Boot приложение.
 * Аннотация @SpringBootApplication включает автоматическую конфигурацию,
 * сканирование компонентов и настройку бинов.
 */
@SpringBootApplication
public class SkillFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkillFlowApplication.class, args);
    }
}
```

### 3.2. Entity классы (Сущности базы данных)

#### **Expert.java** - сущность эксперта/преподавателя
```java
/**
 * Сущность, представляющая эксперта (преподавателя) в системе SkillFlow.
 * Содержит информацию об эксперте: имя, фамилия, email, биография,
 * специализация и список курсов, которые он ведет.
 * Отображается на таблицу "experts" в базе данных.
 */
@Entity
@Table(name = "experts")
public class Expert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_эксперта")
    private Long id;

    @NotBlank(message = "Имя обязательно")
    @Column(name = "имя_эксперта")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Column(name = "фамилия_эксперта")
    private String lastName;

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    @Column(name = "email_эксперта", unique = true)
    private String email;

    @Column(name = "биография_эксперта")
    private String biography;

    @NotBlank(message = "Специализация обязательна")
    @Column(name = "специализация_эксперта")
    private String specialization;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
    private List<Course> courses;
    
    // Конструкторы, геттеры и сеттеры
}
```

#### **Student.java** - сущность студента
```java
/**
 * Сущность, представляющая студента в системе SkillFlow.
 * Содержит основную информацию о студенте: имя, фамилия, email,
 * дата регистрации, статус и список учебных планов.
 * Отображается на таблицу "students" в базе данных.
 */
@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_студента")
    private Long id;

    @Column(name = "имя_студента")
    private String firstName;

    @Column(name = "фамилия_студента")
    private String lastName;

    @Column(name = "email_студента", unique = true)
    private String email;

    @Column(name = "дата_регистрации_студента")
    private LocalDateTime registrationDate;

    @Column(name = "статус_студента")
    private String status; // "АКТИВЕН", "ЗАВЕРШИЛ", "ОТЧИСЛЕН", "В_ОЖИДАНИИ"

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudyPlan> studyPlans;
    
    // Конструкторы, геттеры и сеттеры
}
```

#### **Course.java** - сущность курса
```java
/**
 * Сущность, представляющая курс в системе SkillFlow.
 * Содержит информацию о курсе: название, описание, даты проведения,
 * эксперта (преподавателя), статус и связанные объекты (цены, учебные планы).
 * Отображается на таблицу "courses" в базе данных.
 */
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_курса")
    private Long id;

    @NotBlank(message = "Название курса обязательно")
    @Size(min = 3, max = 100, message = "Название курса должно быть от 3 до 100 символов")
    @Column(name = "название_курса")
    private String title;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    @Column(name = "описание_курса")
    private String description;

    @Column(name = "дата_создания_курса")
    private LocalDateTime creationDate;

    @Column(name = "дата_обновления_курса")
    private LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "id_эксперта")
    private Expert expert;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<CoursePrice> prices;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<StudyPlan> studyPlans;

    @Column(name = "статус_курса")
    @NotBlank(message = "Статус обязателен")
    private String status; // "АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"

    @Column(name = "начало_регистрации")
    private LocalDateTime registrationStart;

    @Column(name = "окончание_регистрации")
    private LocalDateTime registrationEnd;

    @Column(name = "начало_курса")
    private LocalDateTime courseStart;

    @Column(name = "окончание_курса")
    private LocalDateTime courseEnd;
    
    // Конструкторы, геттеры и сеттеры
}
```

#### **StudyPlan.java** - сущность учебного плана
```java
/**
 * Сущность, представляющая учебный план в системе SkillFlow.
 * Связывает студента с курсом и содержит информацию о датах обучения
 * и статусе прохождения. Отображается на таблицу "study_plans" в базе данных.
 */
@Entity
@Table(name = "study_plans")
public class StudyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_плана")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_студента")
    @NotNull(message = "Студент обязателен")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_курса")
    @NotNull(message = "Курс обязателен")
    private Course course;

    @Column(name = "дата_начала")
    private LocalDateTime startDate;

    @Column(name = "плановая_дата_окончания")
    private LocalDateTime plannedEndDate;

    @Column(name = "статус_плана")
    private String status;
    
    // Конструкторы, геттеры и сеттеры
}
```

#### **CoursePrice.java** - сущность цены курса
```java
/**
 * Сущность, представляющая цену курса в системе SkillFlow.
 * Содержит информацию о цене курса на определенный период времени.
 * Позволяет устанавливать разные цены для курса в разные периоды.
 * Отображается на таблицу "course_prices" в базе данных.
 */
@Entity
@Table(name = "course_prices")
public class CoursePrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_курса")
    @NotNull(message = "Курс обязателен")
    private Course course;

    @Column(name = "дата_начала_действия_цены")
    @NotNull(message = "Дата начала действия обязательна")
    private LocalDateTime startDate;

    @Column(name = "цена_курса")
    @DecimalMin(value = "0.0", message = "Цена должна быть положительной")
    private BigDecimal price;

    @Column(name = "дата_окончания_действия_цены")
    private LocalDateTime endDate;
    
    // Конструкторы, геттеры и сеттеры
}
```

### 3.3. Repository интерфейсы (Доступ к данным)

#### **ExpertRepository.java**
```java
/**
 * Репозиторий для работы с экспертами в базе данных.
 * Предоставляет методы для выполнения операций с сущностью Expert.
 * Наследует JpaRepository, что дает базовые CRUD операции.
 */
@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long> {
    List<Expert> findBySpecialization(String specialization);
    List<Expert> findByLastNameContainingIgnoreCase(String lastName);
    Expert findByEmail(String email);
}
```

#### **StudentRepository.java**
```java
/**
 * Репозиторий для работы со студентами в базе данных.
 * Предоставляет методы для выполнения операций с сущностью Student.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByLastNameContainingIgnoreCase(String lastName);
    Student findByEmail(String email);
    List<Student> findByStatus(String status);
    
    @Query("SELECT s FROM Student s WHERE s.registrationDate BETWEEN :startDate AND :endDate")
    List<Student> findByRegistrationDateBetween(@Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);
}
```

#### **CourseRepository.java**
```java
/**
 * Репозиторий для работы с курсами в базе данных.
 * Предоставляет методы для выполнения операций с сущностью Course.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByExpertId(Long expertId);
    List<Course> findByStatus(String status);
    List<Course> findByCreationDateBetween(LocalDateTime start, LocalDateTime end);
    List<Course> findByStatusAndRegistrationEndAfter(String status, LocalDateTime date);
    
    @Query("SELECT c FROM Course c WHERE " +
        "(:status IS NULL OR c.status = :status) AND " +
        "(:expertId IS NULL OR c.expert.id = :expertId) AND " +
        "(:startDate IS NULL OR c.creationDate >= :startDate) AND " +
        "(:endDate IS NULL OR c.creationDate <= :endDate)")
    List<Course> findWithFilters(@Param("status") String status,
        @Param("expertId") Long expertId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);
}
```

#### **StudyPlanRepository.java**
```java
/**
 * Репозиторий для работы с учебными планами в базе данных.
 * Предоставляет методы для выполнения операций с сущностью StudyPlan.
 */
@Repository
public interface StudyPlanRepository extends JpaRepository<StudyPlan, Long> {
    List<StudyPlan> findByStudentId(Long studentId);
    List<StudyPlan> findByCourseId(Long courseId);
    List<StudyPlan> findByStatus(String status);
    List<StudyPlan> findByStudentIdAndCourseId(Long studentId, Long courseId);
}
```

### 3.4. Service классы (Бизнес-логика)

#### **ExpertService.java**
```java
/**
 * Сервисный класс для бизнес-логики работы с экспертами.
 * Содержит методы для управления экспертами: создание, обновление, удаление,
 * поиск и валидация. Все методы выполняются в транзакционном контексте.
 */
@Service
@Transactional
public class ExpertService {
    @Autowired
    private ExpertRepository expertRepository;

    public List<Expert> getAllExperts() {
        return expertRepository.findAll();
    }

    public Optional<Expert> getExpertById(Long id) {
        return expertRepository.findById(id);
    }

    public Expert saveExpert(Expert expert) {
        // Валидация данных
        if (expert.getFirstName() == null || expert.getFirstName().trim().isEmpty()) {
            throw new RuntimeException("Имя не может быть пустым");
        }
        // Проверка уникальности email
        Expert existingExpert = expertRepository.findByEmail(expert.getEmail());
        if (existingExpert != null &&
            (expert.getId() == null || !existingExpert.getId().equals(expert.getId()))) {
            throw new RuntimeException("Эксперт с таким email уже существует");
        }
        return expertRepository.save(expert);
    }
    
    // Другие методы
}
```

#### **CourseService.java**
```java
/**
 * Сервисный класс для бизнес-логики работы с курсами.
 * Содержит методы для управления курсами: создание, обновление, удаление,
 * поиск и валидация. Все методы выполняются в транзакционном контексте.
 */
@Service
@Transactional
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course) {
        // Валидация перед сохранением
        validateCourse(course);
        return courseRepository.save(course);
    }

    private void validateCourse(Course course) {
        if (course.getTitle() == null || course.getTitle().trim().length() < 3) {
            throw new IllegalArgumentException("Название курса должно содержать минимум 3 символа");
        }
        // Валидация дат
        if (course.getRegistrationStart() != null && course.getRegistrationEnd() != null &&
            !course.getRegistrationEnd().isAfter(course.getRegistrationStart())) {
            throw new IllegalArgumentException("Дата окончания регистрации должна быть после даты начала");
        }
    }
    
    // Другие методы
}
```

### 3.5. Controller классы (Веб-слой)

#### **ExpertController.java** - CRUD операции для экспертов
```java
/**
 * Контроллер для управления экспертами (преподавателями) в системе SkillFlow.
 * Обрабатывает операции с экспертами: создание, редактирование,
 * удаление и просмотр. Использует валидацию данных через аннотации Bean Validation.
 */
@Controller
@RequestMapping("/experts")
public class ExpertController {
    @Autowired
    private ExpertService expertService;

    @GetMapping
    public String getAllExperts(Model model) {
        model.addAttribute("experts", expertService.getAllExperts());
        return "experts/list";
    }

    @GetMapping("/{id}")
    public String getExpertById(@PathVariable Long id, Model model) {
        Optional<Expert> expert = expertService.getExpertById(id);
        if (expert.isPresent()) {
            model.addAttribute("expert", expert.get());
            return "experts/view";
        }
        return "redirect:/experts";
    }

    @GetMapping("/new")
    public String showExpertForm(Model model) {
        model.addAttribute("expert", new Expert());
        return "experts/form";
    }

    @PostMapping("/create")
    public String createExpert(@Valid @ModelAttribute Expert expert, BindingResult result) {
        if (result.hasErrors()) {
            return "experts/form";
        }
        try {
            expertService.saveExpert(expert);
            return "redirect:/experts";
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.expert", e.getMessage());
            return "experts/form";
        }
    }
    
    // Другие методы
}
```

#### **CourseController.java** - CRUD операции для курсов
```java
/**
 * Контроллер для управления курсами в системе SkillFlow.
 * Обрабатывает HTTP-запросы, связанные с курсами: создание, редактирование,
 * удаление, просмотр и управление статусами.
 */
@Controller
@RequestMapping("/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private CourseService courseService;
    @Autowired
    private ExpertService expertService;

    @GetMapping
    public String getAllCourses(Model model,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "startDate", required = false) String startDate,
        @RequestParam(value = "endDate", required = false) String endDate) {
        
        List<Course> courses;
        if (status != null && !status.isEmpty()) {
            courses = courseService.getCoursesByStatus(status);
        } else if (startDate != null && endDate != null) {
            try {
                LocalDateTime start = LocalDate.parse(startDate, DATE_FORMATTER).atStartOfDay();
                LocalDateTime end = LocalDate.parse(endDate, DATE_FORMATTER).atTime(23, 59, 59);
                courses = courseService.getCoursesByDateRange(start, end);
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "Неверный формат даты");
                courses = courseService.getAllCourses();
            }
        } else {
            courses = courseService.getAllCourses();
        }

        model.addAttribute("courses", courses);
        model.addAttribute("statuses", List.of("АКТИВЕН", "НЕ_АКТИВЕН", "В_РАЗРАБОТКЕ"));
        model.addAttribute("experts", expertService.getAllExperts());
        return "courses/list";
    }
    
    // Другие методы
}
```

#### **StudyPlanController.java** - управление учебными планами
```java
/**
 * Контроллер для управления учебными планами в системе SkillFlow.
 * Учебный план связывает студента с курсом и содержит информацию
 * о датах обучения и статусе прохождения.
 */
@Controller
@RequestMapping("/studyplans")
public class StudyPlanController {
    @Autowired
    private StudyPlanService studyPlanService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;

    @GetMapping
    public String getAllStudyPlans(Model model) {
        try {
            model.addAttribute("studyPlans", studyPlanService.getAllStudyPlans());
            model.addAttribute("statuses", List.of("активен", "завершен", "приостановлен", "отменен"));
            return "studyplans/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при загрузке учебных планов: " + e.getMessage());
            return "error/error";
        }
    }
    
    // Другие методы
}
```

### 3.6. Обработка исключений

#### **GlobalExceptionHandler.java**
```java
/**
 * Глобальный обработчик исключений для всего приложения SkillFlow.
 * Этот класс обрабатывает исключения, возникающие в контроллерах,
 * и возвращает соответствующие страницы ошибок.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BindException.class)
    public String handleValidationExceptions(BindException ex, Model model) {
        model.addAttribute("errors", ex.getBindingResult().getAllErrors());
        model.addAttribute("errorMessage", "Ошибка валидации данных");
        return "error/validation-error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Произошла ошибка: " + ex.getMessage());
        return "error/error";
    }
}
```

## 4. Конфигурационные файлы

### **application.properties**
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/skillflow
spring.datasource.username=postgres
spring.datasource.password=5432

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true

# Server
server.port=8080

# Thymeleaf
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.cache=false

# Logging
logging.level.root=INFO
logging.level.com.skillflow=DEBUG
```

## 5. HTML шаблоны

Реализовано более 20 HTML шаблонов с использованием Thymeleaf и Bootstrap 5:

### Основные страницы:
- **index.html** - главная страница с навигацией и статистикой
- **experts/** - полный CRUD для экспертов (list, form, view)
- **students/** - полный CRUD для студентов (list, form, view)
- **courses/** - полный CRUD для курсов (list, form, view)
- **studyplans/** - полный CRUD для учебных планов (list, form, view)
- **error/** - страницы ошибок (error.html, validation-error.html)

### Особенности интерфейса:
- ✅ Адаптивный дизайн (mobile-first)
- ✅ Интерактивные анимации (animations.js)
- ✅ Валидация форм на стороне клиента
- ✅ Хлебные крошки для навигации
- ✅ Статистические карточки
- ✅ Модальные подтверждения удаления
- ✅ Tooltips и всплывающие подсказки

## 6. JavaScript функциональность

### **animations.js**
```javascript
/**
 * Файл анимаций для системы SkillFlow.
 * Содержит JavaScript код для анимации элементов интерфейса.
 */
document.addEventListener('DOMContentLoaded', function() {
    // Анимация появления элементов при скролле
    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, { threshold: 0.1 });

    // Утилиты для работы с датами
    const SkillFlowUtils = {
        formatDate: function(dateString) {
            const options = { year: 'numeric', month: 'long', day: 'numeric' };
            return new Date(dateString).toLocaleDateString('ru-RU', options);
        },
        
        formatRelativeTime: function(dateString) {
            const date = new Date(dateString);
            const now = new Date();
            const diffDays = Math.ceil(Math.abs(now - date) / (1000 * 60 * 60 * 24));
            
            if (diffDays === 1) return 'Сегодня';
            if (diffDays === 2) return 'Вчера';
            if (diffDays < 7) return `${diffDays} дня назад`;
            return this.formatDate(dateString);
        }
    };
});
```

## 7. Схема базы данных

```sql
-- Основные таблицы
experts (id_эксперта, имя_эксперта, фамилия_эксперта, email_эксперта, биография_эксперта, специализация_эксперта)
students (id_студента, имя_студента, фамилия_студента, email_студента, дата_регистрации_студента, статус_студента)
courses (id_курса, название_курса, описание_курса, дата_создания_курса, дата_обновления_курса, id_эксперта, статус_курса, начало_регистрации, окончание_регистрации, начало_курса, окончание_курса)
study_plans (id_плана, id_студента, id_курса, дата_начала, плановая_дата_окончания, статус_плана)
course_prices (id, id_курса, дата_начала_действия_цены, цена_курса, дата_окончания_действия_цены)

-- Связи
courses.id_эксперта → experts.id_эксперта
study_plans.id_студента → students.id_студента
study_plans.id_курса → courses.id_курса
course_prices.id_курса → courses.id_курса
```

## 8. Инструкция по запуску

### Требования:
- Java JDK 11+
- Maven 3.8+
- PostgreSQL 14+
- Node.js (для фронтенд зависимостей)

### Установка и запуск:

1. **Клонирование репозитория:**
```bash
git clone https://github.com/AndreiPanchenko/SkillFlow.git
cd SkillFlow
```

2. **Настройка базы данных:**
```sql
CREATE DATABASE skillflow;
CREATE USER skillflow_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE skillflow TO skillflow_user;
```

3. **Настройка конфигурации:**
Отредактируйте `src/main/resources/application.properties`:
```properties
spring.datasource.username=skillflow_user
spring.datasource.password=your_password
```

4. **Сборка и запуск:**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Доступ к приложению:**
- Главная страница: http://localhost:8080
- Админ панель: http://localhost:8080

### Основные URL:
- `/` - главная страница
- `/experts` - управление экспертами
- `/students` - управление студентами
- `/courses` - управление курсами
- `/studyplans` - управление учебными планами
