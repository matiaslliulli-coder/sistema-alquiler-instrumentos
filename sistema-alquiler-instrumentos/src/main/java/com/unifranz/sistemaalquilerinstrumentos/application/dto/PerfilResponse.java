package com.unifranz.sistemaalquilerinstrumentos.application.dto;

import com.unifranz.sistemaalquilerinstrumentos.domain.Usuario;
import com.unifranz.sistemaalquilerinstrumentos.domain.Permiso;
import java.util.List;

public record PerfilResponse(
        Integer idUsuario,
        String nombreUsuario,
        String nombreCompleto,
        String correo,
        String rol,
        Integer codTienda,
        List<String> permisos) {

    public static PerfilResponse desde(Usuario u) {
        List<String> permisos = u.getTipoUsuario().getPermisos().stream()
                .map(Permiso::getNombrePermiso).sorted().toList();
        return new PerfilResponse(u.getIdUsuario(), u.getNombreUsuario(), u.getNombreCompleto(), u.getCorreo(),
                u.getTipoUsuario().getNombreTipo(), u.getCodTienda(), permisos);
    }
}
