# Semana 2 – Flujo de trabajo con Git y GitHub

## Repositorio
Un repositorio con una rama por integrante y la rama protegida `main`:

```
main                  ← solo código revisado (se actualiza con Pull Request)
├── matias-inventario
├── leonardo-alquileres
├── camila-frontend
└── ricardo-seguridad
```

## Comandos básicos
```bash
git clone https://github.com/<organizacion>/<repositorio>.git
git checkout -b ricardo-seguridad          # crear mi rama
git add .
git commit -m "feat(seguridad): login JWT y BCrypt"
git push -u origin ricardo-seguridad       # subir mi rama
git pull origin main                       # traer cambios de main antes de integrar
```

## Convención de mensajes
`feat:` funcionalidad nueva · `fix:` corrección · `docs:` documentación · `test:` pruebas · `refactor:` mejora interna.

## Reglas
1. Nunca subir contraseñas, claves JWT/AES ni credenciales AWS (el `.gitignore` ya excluye `.env`).
2. Cada sábado, antes de la presentación, hacer `push` de la rama con lo avanzado en la semana.
3. Integrar a `main` por Pull Request revisado por otro integrante.
