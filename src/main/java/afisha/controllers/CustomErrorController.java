package afisha.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class CustomErrorController implements ErrorController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";
    private static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    private static final String ERROR_MESSAGE = "javax.servlet.error.message";
    private static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Получаем статус ошибки
        Object status = request.getAttribute(ERROR_STATUS_CODE);
        Object exception = request.getAttribute(ERROR_EXCEPTION);
        Object message = request.getAttribute(ERROR_MESSAGE);
        Object requestUri = request.getAttribute(ERROR_REQUEST_URI);

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = "Произошла непредвиденная ошибка";
        String errorCode = "500";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            httpStatus = HttpStatus.valueOf(statusCode);
            errorCode = String.valueOf(statusCode);

            switch (statusCode) {
                case 400:
                    errorMessage = "Некорректный запрос";
                    break;
                case 401:
                    errorMessage = "Требуется авторизация";
                    break;
                case 403:
                    errorMessage = "Доступ запрещен";
                    break;
                case 404:
                    errorMessage = "Страница не найдена";
                    break;
                case 405:
                    errorMessage = "Метод не разрешен";
                    break;
                case 408:
                    errorMessage = "Время ожидания истекло";
                    break;
                case 500:
                    errorMessage = "Внутренняя ошибка сервера";
                    break;
                case 502:
                    errorMessage = "Ошибка шлюза";
                    break;
                case 503:
                    errorMessage = "Сервис недоступен";
                    break;
                case 504:
                    errorMessage = "Время ожидания шлюза истекло";
                    break;
            }
        }

        // Логирование ошибки (можно добавить логирование в файл или БД)
        logError(errorCode, requestUri, exception, message);

        // Добавляем данные в модель
        model.addAttribute("errorCode", errorCode);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("timestamp", LocalDateTime.now().format(formatter));
        model.addAttribute("path", requestUri != null ? requestUri.toString() : "Неизвестно");
        model.addAttribute("status", httpStatus.value());

        if (exception != null) {
            model.addAttribute("exception", exception.getClass().getName());
        }

        if (message != null) {
            model.addAttribute("message", message.toString());
        }

        // Возвращаем соответствующую страницу в зависимости от типа ошибки
        if (httpStatus == HttpStatus.NOT_FOUND) {
            return "error/404";
        } else if (httpStatus == HttpStatus.FORBIDDEN) {
            return "error/403";
        } else if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            return "error/500";
        } else {
            return "error/general";
        }
    }

    @GetMapping("/404")
    public String custom404Page(Model model) {
        model.addAttribute("errorCode", "404");
        model.addAttribute("errorMessage", "Страница не найдена");
        model.addAttribute("timestamp", LocalDateTime.now().format(formatter));
        return "error/404";
    }

    @GetMapping("/403")
    public String custom403Page(Model model) {
        model.addAttribute("errorCode", "403");
        model.addAttribute("errorMessage", "Доступ запрещен");
        model.addAttribute("timestamp", LocalDateTime.now().format(formatter));
        return "error/403";
    }

    @GetMapping("/500")
    public String custom500Page(Model model) {
        model.addAttribute("errorCode", "500");
        model.addAttribute("errorMessage", "Внутренняя ошибка сервера");
        model.addAttribute("timestamp", LocalDateTime.now().format(formatter));
        return "error/500";
    }

    private void logError(String errorCode, Object requestUri, Object exception, Object message) {
        String logMessage = String.format(
                "[ERROR] Code: %s | URI: %s | Exception: %s | Message: %s | Time: %s",
                errorCode,
                requestUri != null ? requestUri : "N/A",
                exception != null ? exception : "N/A",
                message != null ? message : "N/A",
                LocalDateTime.now().format(formatter)
        );

        System.err.println(logMessage);
        // Здесь можно добавить логирование в файл или БД
    }

    public String getErrorPath() {
        return "/error";
    }
}