# Java Autotest Template

Проект-пустышка. Готовый к заполнению автотестами.  
Особенности: Настроен для работы с Allure, и многопоточностью JUnit. UI тесты настроены для запуска в Selenoid. (Не
забываем настроить соответствующие переменные в файле pom.xml). Перезапуск упавших тестов.

Есть возможность отправки тестранов в Jira (TM4J).

## <a href="#" name="content"></a> Оглавление

- [Стек проекта](#projectStack)
- [Необходимые инструменты](#neededStuff)
- [Запуск через Maven](#runProjectByMaven)
- [Запуск тестов локально](#runProjectLocaly)
- [Написание тестов](#writingTests)
  + [Общее положение](#generalRules)
  + [Тест-Сюиты](#testSuites)
  + [UI тесты](#uiTests)
  + [API тесты](#apiTests)
  + [Аннотации](#annotations)
  + [Наименования классов](#classNaming)
  + [Code Style](#codeStyle)
- [Git](#git)
  + [Клонирование проекта](#projectCloning)
  + [Workflow](#workflow)
  + [Работа с ветками](#branches)
  + [Новая ветка](#newBranch)
  + [Получение чужой ветки](#someonesBranch)
  + [Получение изменений ветки](#getBranchChanges)
  + [Получение изменений из мастера в свою ветку](#getMasterChangesToBranch)
  + [Конфигурация](#gitSettings)


## Стек проекта <a href="#" name="projectStack">↑</a>

- Java 8
- JUnit 5
- Selenide (UI)
- RestAssured (API)
- Allure
- Selenoid


## Необходимые инструменты <a href="#" name="neededStuff">↑</a>

- [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/download/)

- [Git](https://git-scm.com/downloads)

- [Maven](https://maven.apache.org/download.cgi)

- [ChromeDriver](https://chromedriver.chromium.org/) - ВАЖНО! Качаем только стабильную версию для платформы x86 (
  32-битную)

Директории бинарных файлов **Maven** и **ChromeDriver** должны быть добавлены в переменную системного окружения **PATH**.


## Запуск через Maven <a href="#" name="runProjectByMaven">↑</a>

Все команды выполнять перейдя в директорию проекта через командную строку.

Проверка наличия maven `mvn -v`.

Сборка проекта `mvn clean install`.

Запуск тестов `mvn clean test`.

Запуск отдельного класса/пакета:

```shell
# Запуск метода указанного класса
mvn -Dtest="SomeClass#someMethod" test 

# Запуск методов указанного класса с маской
mvn -Dtest="SomeClass#*Method" test 

# Запуск класса указанного пакета
mvn -Dtest="com.example.SomeClass" test 

# Запуск классов подходящих под маску для указанного пакета
mvn -Dtest="com.example.*Class" test

# Запуск всех классов указанного пакета
mvn -Dtest="com/example/**" test 

# Все UI тесты
mvn -Dgroups="UITest" -Dwebdriver.remote.hub="http://урл_селеноида:4444/wd/hub" -Dweb.host.url="https://урл_стенда" test

# Все API тесты
mvn -Dgroups="APITest" -Dapi.host.url="https://урл_api_стенда" test
```

Генерация отчета **Allure** `mvn allure:report`, после переходим по пути `/target/site` и открываем файл `index.html`.


## Запуск тестов локально <a href="#" name="runProjectLocaly">↑</a>

Клонируем проект и выполняем его сборку. Сборка должна проходить без ошибок.

По умолчанию запуск тестов выполняется в многопоточном режиме, а UI тесты настроены для выполнения в Selenoid.

Для локального запуска тестов необходимо будет переопределить системные переменные, чтобы тесты выполнялись в один
поток, а запуск UI тестов происходил на локальной машине.

Для этого:

1. Открываем проект в IDEA.
2. Открываем меню `Run -> Edit Configurations...`
3. В открывшемся окне раскрываем список **Templates**
4. Выбираем в списке **JUnit**.
5. В строку **VM Options**, та что с текстом `-ea`, вставляем
   текст `-ea -Dwebdriver.remote=false -Dwebdriver.window.maximize=true -Dallure.send.results=false -Djira.send.results=false`
6. Удаляем предыдущие пресеты запуска из списка JUnit, тот что над списком **Templates**

Строка **VM Options** может содержать и другие переопределения системных параметров из файла `pom.xml` необходимых для
отладки тестов локально.

**Важно!** Все вышеописанные действия необходимы, чтобы избежать изменения файла `pom.xml`, так как они могут быть
случайно отправлены в репозиторий.


## Написание тестов <a href="#" name="writingTests">↑</a>

### Общее положение <a href="#" name="generalRules">↑</a>

Все тесты должны быть реализованы в формате классов `тест -> шаги теста -> класс методов теста`.

В самом классе теста присутствуют лишь шаги (Steps) и проверки (Assertions).

Шаги состоят из отдельных более атомарных действий прописанных в классе с тестовыми методами. В шагах не производим
никаких отдельных действий, только вызов тестовых методов.

В классе с тестовыми методами располагаются конкретные атомарные действия.

**Q: Зачем нужны шаги, если все действия можно реализовать в классе тестовых методов?**  
A: _Ответ прост, если этого не сделать, шаги состоящие из тех же самых атомарных действий будут располагаться в классе с
тестовыми методами, что будет приводить к разрастанию данного класса с каждым новым тестом. Классы с шагами напротив
возьмут на себя все действия и разграничат их между тестами, что оставит в классе тестовых методов только необходимое, а
для каждого тест-кейса будут свои шаги._


### Тест-Сюиты <a href="#" name="testSuites">↑</a>

Тест-Сюиты предназначены для запуска группы тестов представленных в виде отдельных классов, пакетов с классами, или
методов отдельных классов.

Тест-Сюиты в проекте представлены в виде отдельных классов.

Каждый тест-сюит это обычный класс, с именем **...Suite** вызывающий статические методы класса **TestRunner**,
и реализовать тестовый метод, который будет иметь осмысленное имя и суффикс "...Suite()".

Класс тест-сюита позволяет запустить пакет, отдельные классы, и методы отдельного класса.

Используйте данные методы для составления тест-сюита в тестовом методе.

```java
import static ru.bootdev.test.core.TestRunner.*;

public class SomeSuite {

    @Test
    void testSuite() {
        runPackage("ru.bootdev.test.ui", "*Test.*");
        runClass(SomeTest.class, AnotherTest.class);
        runMethod(SomeClass.class, "methodName", "anotherMethodName");
    }
}
```

Тест-сюиты предназначены для более удобной группировки тестов, использование их не обязательное, так как задать запуск
классов, можно через Maven.


### UI тесты <a href="#" name="uiTests">↑</a>

Для исполнения UI тестов используется библиотека Selenide. Все UI тесты построены по PageObject с использованием шагов.
Необходимость использования шагов описана в [Общих положениях](#Общее-положение).

Как построен PageObject - `класс тестов -> класс шагов -> класс страницы`. Класс страницы содержит описание страницы, и
методов для работы с ними. Все остальное идентично [Общим положениям](#Общее-положение).

ВАЖНО! При описании страницы **НЕ НУЖНО** описывать ее всю целиком. Описываем только то, что необходимо для реализации
описываемого тест-кейса. Лишний код "на будущее" не приемлем в страницах.

Все страницы должны так или иначе должны наследоваться от базовой страницы (**BasePage**) в которой расположены
обобщенные методы для всех страниц.

Пример `test -> steps -> page -> page -> BasPage`.

Пример класса страницы:

```java
public class SomePage extends BasePage {

    // В верхней части страницы описываем все статические селекторы которые будут использоваться в методах ниже
    @FindBy(className = "...")
    protected SelenideElement pageElementOne;

    @FindBy(id = "...")
    protected SelenideElement pageElementTwo;

    @FindBy(xpath = "...")
    protected SelenideElement pageElementThree;

    // После описываем методы-селекторы
    protected SelenideElement getLinkByName(String name) {
        return $(String.format("//a[text()=%s]", name));
    }

    // После описания селекторов описываем методы для работы с ними

    public void clickElementOne() {
        pageElementOne.click();
    }

    // ...

    public void clickLinkByName(String name) {
        getLinkByN(name).click();
    }

    // ...
}
```

Пример класса шагов:

```java
public class SomeSteps extends BaseSteps {

    // Объявляем используемые страницы
    protected SomePage somePage = new SomePage();
    protected AnotherPage anotherPage = new AnotherPage();

    // Реализуем шаги 

    @Step("Step one")
    public void stepOne() {
        somePage.open();
        somePage.clickSomething();
        somePage.enterTextSomewhere();
    }

    @Step("Step two")
    public void stepTwo() {
        anotherPage.doSomething();
    }

    @Step("Step three")
    public Boolean stepThree() {
        return anotherPage.checkSomeLogic();
    }

    // ...
}
```

При описании тестового класса ОБЯЗАТЕЛЬНО аннотируем весь класс аннотацией **@UITests**.

Пример тестового класса:

```java
@UITest
public class TestClassName {

    private SomeSteps someSteps;

    @BeforeEach
    private void setUpTest() {
        someSteps = new SomeSteps();
    }

    @Test
    void testOne() {
        someSteps.stepOne();
        someSteps.stepTwo();
        Boolean isSomethingHappened = someSteps.stepThree();

        assertThat(isSomethingHappened).as("It seems something is not happened").isTrue();
    }

    @Test
    void testTwo() {
        someSteps.foo();
        Boolean isSomethingHappened = someSteps.bar();

        assertThat(isSomethingHappened).as("It seems something is not happened").isTrue();
    }
}
```

ВАЖНО! При проектировании тестового класса учитывайте, что каждый тестовый метод должен быть самодостаточным. Тестов
зависимых от других тестов быть не должно. Во-первых, это связано с тем, что JUnit выполняет тестовые методы в случайном
порядке, во-вторых, все методы выполняются в многопоточном режиме. То есть каждый тестовый метод исполняется в отдельном
потоке, имейте это ввиду при проектировании тестов.


### API тесты <a href="#" name="apiTests">↑</a>

Для исполнения API тестов используется библиотека RestAssured. Структура тестов как и прежде строится по аналогии
указанных в [Общих положениях](#Общее-положение).

`тестовый класс -> класс шагов -> класс запросов`

Важное уточнение! Данная цепочка может быть сокращена до варианта `тестовый класс -> класс шагов`. При этом необходимые
запросы будут расположены в методах класса шагов.

Кроме этого, если реализуемый тест-кейс позволяет обойтись без шагов, то просто выполняемы необходимый запрос в тестовом
классе, либо напрямую, либо используя класс запросов.

При описании тестового класса ОБЯЗАТЕЛЬНО аннотируем весь класс аннотацией **@APITests**.

```java
@APITest
class TestClassName {
    // ...
}
```

ВАЖНО! При проектировании тестового класса учитывайте, что каждый тестовый метод должен быть самодостаточным. Тестов
зависимых от других тестов быть не должно. Во-первых, это связано с тем, что у JUnit выполняет тестовые методы в
случайном порядке, во-вторых, все методы выполняются в многопоточном режиме.


### Аннотации <a href="#" name="annotations">↑</a>

Вне зависимости от типа исполняемого теста, будь то API или UI тест, тестовый метод должен быть должен быть аннотирован
дополнительными аннотациями.

Набор аннотаций представлен фреймворками/библиотеками JUnit, Allure и Jira.

Пример тестового класса с одним тестовым методом

```java
import io.qameta.allure.*;

@UITest // Аннотация проекта, отмечающая что данный тестовый класс является UI тестом [ОБЯЗАТЕЛЬНАЯ]
public class SomeTest {

    // Аннотация JUnit отмечающая метод как тестовый
    @Test   // [Обязательная]
    // Аннотация отмечает к какому тест-кейсу принадлежит тестовый метод в Jira, а так же 
    // для создания отчета который будет направлен в Jira в качестве результатов тестового прогона.
    // В качестве идентификатора теста можно использовать либо параметр key (желательно) либо name
    @ТestCase(key = "PROJECT-T863", name = "Проверка получения результатов от автотеста") // [Необязательна]
    // Ссылка на реализуемый тест-кейс в Jira
    // Так как проект уже содержит шаблон ссылки, достаточно указать в качестве параметра лишь ID тест-кейса
    @TmsLink("PROJECT-T863") // [Необязательна]
    // Ссылка на созданную таску в Jira
    // Так как проект уже содержит шаблон ссылки, достаточно указать в качестве параметра лишь ID таски
    @Issue("PROJECT-2429")  // [По необходимости]
    // Важность тест-кейса
    @Severity(SeverityLevel.BLOCKER) // [Желательная]
    // Владелец
    @Owner("Имя Фамилия Отчество") // [По необходимости]
    // Описание фичи
    @Feature("...") // [По необходимости]
    // Описание стори
    @Epic("...") // [По необходимости]
    // Описание эпика
    @Story("...") // [По необходимости]
    // Описание теста, то что будет отображаться вместо имени метода в отчете Allure
    @Description("Описание выполняемого теста") // [Желательная]
    void PROJECT_T999_test() { // !!! Обязательный формат имени тестового метода
        // ...
    }
    
    // Стандартный набор аннотаций
    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Owner("Имя Фамилия Отчество")
    @Description("Описание выполняемого теста")
    void PROJECT_T1000_test() {
        // ...
    }

    // Минимальный набор аннотаций
    @Test
    @Description("Описание выполняемого теста")
    void PROJECT_T1001_test() {
        // ...
    }
}
```

Аннотации для шагов выглядят следующим образом

```java
import io.qameta.allure.Step;

public class SomeSteps {

    // Описание выполняемого шага
    @Step("Описание шага в которое можно передать значение аргументов метода {user} и {pass}")
    public void stepOne(String user, String pass) {
        // ...
    }
}
```


### Наименование классов <a href="#" name="classNaming">↑</a>

Все классы должны именоваться по [CamlCase](https://ru.wikipedia.org/wiki/CamelCase).

Класс в котором реализуется тест должен обязательно иметь суффикс `...Test`. Пример `PROJECTT123Test`.

Класс в котором реализуется шаги должен обязательно иметь суффикс `...Steps`. Пример `PROJECTT123Steps`.

Класс в котором реализуется API запрос должен обязательно иметь суффикс `...Request`. Пример `PROJECTT123Request`.

Класс в котором реализуется модель данных должен обязательно иметь суффикс `...Model`. Пример `BigDataRequestModel`
, `BigDataResponseModel`, `ClientModel`, `CustomerModel` и т.д.

Класс в котором реализуется UI страница должен обязательно иметь суффикс `...Page`. Пример `MainPage`.

Класс в котором реализуется UI модальное окно должен обязательно иметь суффикс `...ModalPage`.
Пример `ChangeSettingsModalPage`.

Класс в котором реализуется UI секция страницы (например header) окно должен обязательно иметь суффикс `...PageSection`.
Пример `HeaderPageSection`.


### Code Style <a href="#" name="codeStyle">↑</a>

В коде не должно быть комментариев, которые указывают на очевидные вещи, код сам должен говорить за себя.

В коде не должно быть закомментированных участков кода, используйте для экспериментов ветки и коммиты git.

Код не должен повторяться. Это касается как содержания классов, так и проекта в целом.

Код должен быть красиво структурирован. Отдельные смысловые блоки должны отделяться пустыми строками. Так же код не
должен быть длинным. Разбивайте длинные строки.

Обязательно используйте авто-форматирование кода (CTRL+ALT+L) и удаление неиспользуемых импортов (CTRL+ALT+O).
Кроме этого, обращайте внимание на панель предупреждений справа сверху листинга.

Код должен быть всегда структурирован по следующему формату

```java
@APITest
public class PrettyClass {

    // Поля класса

    // Конструктор класса

    // Методы класса

    public void someMethod() {
        // Необходимые локальные переменные и модели

        // Код исполнения использующий переменные и модели
    }
} 
```

Небольшой пример класса с отображением отступов

```java
@APITest
public class PrettyClass {

    // ↑↑↑ Обязательный отступ в 1 строку от определения класса
    private String someString;
    private Double someDouble;
    
    @FindBy(xpath = "...")
    private SelenideElement elementOne; // Поля с аннотациями отделяем пустыми строками
    
    @FindBy(xpath = "...")
    private SelenideElement elementOne; // Поля с аннотациями отделяем пустыми строками

    // ↑↑↑ Обязательно отделяем блок описания полей от методов одной пустой строкой
    private void someMethodOne(String text) {
        // ...
    }

    // ↑↑↑ Одна пустая строка от предыдущего метода
    public void someMethodTwo() {
        String text = "...";
        
        someMethodOne(text);
        // ...
    } // ↓↓↓ Отступов в конце класса быть не должно
} 
```


## Git <a href="#" name="git">↑</a>

### Клонирование проекта <a href="#" name="projectCloning">↑</a>

Скачать проект можно выполнив команду `git clone [адрес_репозитория]`.

Важно! Для скачивания по SSH, в профиле репозитория должен быть прописан публичный SSH ключ, сгенерированный
на вашей локальной машине.

Для генерации ключа выполняем команду `ssh-keygen -t rsa`. После переходим в папку пользователя `%USERPROFILE%\.ssh`, и
копируем содержимое файла с суффиксом `..._rsa.pub`. Вставляем скопированный текст в качестве SSH ключа в профиле
Bitbucket.


### Workflow <a href="#" name="workflow">↑</a>

В ветке **master** храним только рабочие и отлаженные тесты.

Каждый новый тест реализуется в отдельной ветке, причем имя ветки должно охарактеризовать реализуемый тест-кейс.

Пример имени ветки `PROJECT-T123`.

После того как тест полностью реализован и отлажен, создается **Pull Request** (**PR**), и отправляется на код ревью. По
прохождении ревью, **PR** вливается (merge) в ветку мастер/дев со сжатием всех коммитов в один (squash) и удалением
вливаемой ветки.

Все, что не касается тестов, такое как разработка нового хелпера, смена архитектруы, рефакторинг, примеры для
демонстрации, и прочее подобное, так же реализуется в отдельных ветках с осмысленным названием указывающим на свое
содержание. После чего оформляются в **PR** и отправляются на код ревью.

**ВАЖНО!** Чтобы не создавать конфликтов самому себе, работаем только последовательно. То есть взяли задачу, создали
ветку, реализовали автотест, отправили **PR**. Ждем одобрения, и только потом переходим к следующей задаче.

**ВАЖНО!** Отправлять одну и туже ветку в **PR** запрещено. Локальная ветка которая уже побывала в **PR** повторно
отправляться не должна.

**ВАЖНО!** PR не должен быть большим, не больше 250 строчек. Чем больше PR тем сложнее его ревьювить, и соответственно
тем больше ошибок может быть пропущено.

Статус изменений текущий ветки относительно ветки master можно вывести следующей командой

```shell
git diff --stat master..
# или
git diff --shortstat master..
```


### Работа с ветками <a href="#" name="branches">↑</a>

#### Новая ветка <a href="#" name="newBranch">↑</a>

Перед созданием новой ветки и последующей работы с ней, обязательно получаем в ветку родитель последние изменения
командой `git pull`.

```shell
git checkout ветка_родитель
git pull --rebase
git checkout -b имя_новой_ветки
git push -u origin имя_новой_ветки
```

Для последующей отправки коммитов ветки в удаленный репозиторий достаточно будет выполнить команду `git push`


#### Получение чужой ветки <a href="#" name="someonesBranch">↑</a>

Чтобы получить и переключиться на ветку созданную кем-другим выполняем следующие команды:

```shell
git fetch
git checkout --track origin/имя_ветки
```


#### Получение изменений ветки <a href="#" name="getBranchChanges">↑</a>

Изменения вашей ветки получить изменения данной ветки можно командами:

```shell
git pull --rebase 
```


#### Получение изменений из мастера в свою ветку <a href="#" name="getMasterChangesToBranch">↑</a>

Есть несколько вариантов получить изменения мастера в свою ветку

Первый. С помощью обычного мержа.

```shell
git checkout master
git pull
git checkout ваша_ветка
git merge --squash master
git commit
```

Второй. С помощью ребейза.

```shell
git fetch
git checkout ваша_ветка
git rebase origin/master
```


### Конфигурация <a href="#content" name="gitSettings">↑</a>

Обязательно прописываем имя пользователя и почту.

```shell
git config --global user.name="Имя Фамилия"
git config --global user.email="email@example.com"
```

Опционально можно так же поменять редактор по умолчанию для git
([подробнее тут](https://git-scm.com/book/ru/v2/Приложение-C%3A-Команды-Git-Настройка-и-конфигурация)).

```shell
git config --global core.editor  "code --wait"
```

Конфигурация **git mergetool**

```shell
git config --global merge.tool vscode
git config --global mergetool.vscode.cmd "code --wait $MERGED"
```

Конфигурация **git difftool**

```shell
git config --global diff.tool vscode
git config --global difftool.vscode.cmd "code --wait --diff $LOCAL $REMOTE"
```

Проверить применение данных настроек можно командой

```shell
git config --global --list
```