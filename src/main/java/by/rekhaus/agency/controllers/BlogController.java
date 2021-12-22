package by.rekhaus.agency.controllers;

import by.rekhaus.agency.model.Post;
import by.rekhaus.agency.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

/*Для передачи значений из БД на определенную страницу (шаблон) необходимо
1. Создать репозиторий для интересующей entity при помощи которой будут осуществлятьс crud действия с сообветствующей таблицей
БД.
2. Созданный репозиторий необходимо в классе контроллера создать перемнную которая будет ссылаться на репозиторий
Делается это при помощи аннотации @Autowired
3. Создать объект на основании класса Iterable. Iterable - это как бы массив данных в котором будут полностью содержаться
данные полученные из определенной таблички в БД.
    3.1. В угловых скобках указываем ту модель (entity) с которой будем работать.
    3.2. Даем этому объекту какое нидудь имя. В этом случае post
    3.3. Обращаемся к репозиторию postRepository переменную которого перед этим инициировали
    3.4. Вызываем у репозитория функцию для поиска всех значений в данной таблице postRepository.findAll()
4. Обращаемся к model
4.1. Вызываем у него метод addAttribute, он нужен для того чтобы передовать в шаблон все найденные
в таблице Post значения. В данном случае передавать их будем по имени "posts" а в качестве заначения
будет ранее созданный массив posts со всеми значениями полученными из таблицы Post
5. Необходимо отразить все записи полученные из posts в шаблоне (на страничке html или jsp

Обработка Post запроса
1. Создаем метод возвращающий строку.
2. Анотируем его аннотацией @PostMapping ("/blog/add") в которой указываем какой URL адрес будем обрабатывать
3. Нам необходимо получать данные из формы отправленной POST запросом для этого:
3.1. В параметрах функции прописываем анотацию @RequestParam
3.2. Указываем какой тип данных получаем и его название
3.3. Создаем объект на основе модели в которую хотим передать полученные параметры. В этом случае это модель Post
3.4. В параметры объекта передаем те значения которые хотим записать в БД. Для этого в самой модели должен быть создан
соответствующий конструктор
3.5. Для сохранения данных обращаемся к нужному репозиторию в этом случае это postRepository который мы встроили в начале
в этот класс. Вызываем функцию save в которую передаем entity post (была создана в п.3.3.)

Обработка запроса для отображения страницы с полным текстом статьи с динамическим параметром для этого:
1. В параметре после /blog/ в фигурных скобках пишем название параметра который будет изменяться в этом случае id (можно
назвать как угодно @GetMapping("/blog/{id}")
2. Полученное из URL  динамическое значение (в этом случае id) используется в параметрах анотации @PathVariable которая в
свою очередь прописывается в параметрах функции. Далее указывается тип переменной и ее название. Именное этой переменной
будет присвоено значение полученное из URL при помощи аннотации @PathVariable.
public String blogDetails (@PathVariable (value = "id") Long postId, Model model)
3. Далее необходимо получить соответствующую запись из БД
3.1. Создаем объект Optional<Post>
3.2. В угловых скобках указываем с какой моделью (entity) будем работать в нашем случае Post
3.3. Даем какое нибудь имя объекту
3.4. Присваеваем следующее значение объекту для этого
    3.4.1. Обращаемся к репозиторию к его функции findById()
    3.4.2. В параметры функции findById() добавляем переменную postId (см. п. 2)
В итоге получаем следующий код Optional<Post> post = postRepository.findById(postId);
4. Для удобства передачи данных в шаблон создадим ArrayList содержащий значения типа Post  - ArrayList <Post> res = new ArrayList<>();
4.1. Обратимся к функции ifPresent объекта post (см. п. 3.4.) При помощи функции ifPresent переводим все из класса
Optional в класс ArrayList Для этого в параметрах ifPresent пишем название ArrayLista (res) потом двоеточие и параметр add
post.ifPresent(res::add);
5. Передаем полученные значения в шаблон - model.addAttribute("allposts", res);
*/


@Controller
public class BlogController {
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/blog")
    public String index(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("title", "Страница блога");
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model) {
        model.addAttribute("title", "Добавить статью");
        return "blog-add";
    }

    //Обработка POST запроса из формы добавления статьи
    @PostMapping("/blog/add")
    public String blogPostAdd(@RequestParam String title,
                              @RequestParam String anons,
                              @RequestParam String full_text,
                              Model model) {
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    // Обработчик запроса для отображения страницы с полным текстом статьи
    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") Long postId, Model model) {
        if (!postRepository.existsById(postId)) {
            return "redirect:/blog";
        }
        model.addAttribute("title", "Полная статья");
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("allposts", res);
        return "blog-details";
    }

    // Обработчик запроса для отображения страницы с редактирование
    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") Long postId, Model model) {
        if (!postRepository.existsById(postId)) {
            return "redirect:/blog";
        }
        model.addAttribute("title", "Редактирование статьи");
        Optional<Post> post = postRepository.findById(postId);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("allposts", res);
        return "blog-edit";
    }

    //Обработка POST запроса из формы редактирование статьи
    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") Long postId,
                                 @RequestParam String title,
                                 @RequestParam String anons,
                                 @RequestParam String full_text,
                                 Model model) {
        Post post = postRepository.findById(postId).orElseThrow(IllegalStateException::new);
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    //Обработка POST запроса из формы удаление статьи
    @PostMapping("/blog/{id}/delete")
    public String blogPostDelete(@PathVariable(value = "id") Long postId,
                                 Model model) {
        Post post = postRepository.findById(postId).orElseThrow(IllegalStateException::new);
        postRepository.delete(post);
        return "redirect:/blog";
    }
}
