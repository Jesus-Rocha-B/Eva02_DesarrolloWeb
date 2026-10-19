package com.tecsup.consultamedica.aop;
import com.tecsup.consultamedica.service.AuditoriaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuditoriaAspect {

    private final AuditoriaService auditoriaService;

    public AuditoriaAspect(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @AfterReturning(value = "@annotation(auditable)", returning = "result")
    public void auditarOperacion(JoinPoint joinPoint, Auditable auditable, Object result) {
        try {
            // 1. Obtener el usuario autenticado del contexto de Spring Security
            String username = "SISTEMA";
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                username = auth.getName();
            }

            // 2. Extraer el ID del registro afectado
            Long registroId = extraerId(result, joinPoint.getArgs());

            // 3. Generar una descripción amigable de la operación
            String detalles = "Se ejecutó " + auditable.operacion() + " sobre " + auditable.entidad() +
                              (registroId != null ? " (ID: " + registroId + ")" : "");

            // 4. Persistir automáticamente la auditoría en MySQL
            auditoriaService.registrarAuditoria(username, auditable.operacion(), auditable.entidad(), registroId, detalles);

        } catch (Exception e) {
            // Evitar que una falla en auditoría detenga la operación principal de la aplicación
            System.err.println("Error al registrar auditoría AOP: " + e.getMessage());
        }
    }

    private Long extraerId(Object result, Object[] args) {
        if (result != null) {
            try {
                Method getIdMethod = result.getClass().getMethod("getId");
                Object idObj = getIdMethod.invoke(result);
                if (idObj instanceof Long) {
                    return (Long) idObj;
                }
            } catch (Exception ignored) {}
        }

        // Si no vino en el retorno, buscar en los argumentos del método (ej: cambiarEstado(Long id))
        if (args != null) {
            for (Object arg : args) {
                if (arg instanceof Long) {
                    return (Long) arg;
                }
            }
        }
        return null;
    }
}
