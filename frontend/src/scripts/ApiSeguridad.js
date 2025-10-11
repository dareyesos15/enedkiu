/**
 * Este archivo contiene funciones para interactuar con la API del microservicio seguridad de Laravel.
*/

//URL base de la API de Laravel (asegurarse de que el puerto coincide).
const API_BASE_URL = 'http://127.0.0.1:8000/api';

/**
 * Envía las credenciales de un usuario para iniciar sesión.
 * @param {object} credentials - Un objeto con el email y la contraseña del usuario.
 * @param {string} credentials.email - El email del usuario.
 * @param {string} credentials.password - La contraseña del usuario.
 * @returns {Promise<object>} Una promesa que se resuelve con los datos de la respuesta de la API (ej. token).
 */
export const login = async (credentials) => {
    // --- Validación de entrada ---
    if (!credentials || typeof credentials !== 'object') {
        throw new Error('Se requiere un objeto de credenciales.');
    }
    if (!credentials.email || typeof credentials.email !== 'string' || credentials.email.trim() === '') {
        throw new Error('El campo "email" es requerido y no puede estar vacío.');
    }
    if (!credentials.password || typeof credentials.password !== 'string' || credentials.password.trim() === '') {
        throw new Error('El campo "password" es requerido y no puede estar vacío.');
    }
    // --- Fin de la validación ---

    try {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            body: JSON.stringify(credentials),
        });

        const data = await response.json();

        if (!response.ok) {
            // Si la respuesta no es exitosa (ej. 401, 422), lanza un error con el mensaje de la API.
            throw new Error(data.message || 'Error al iniciar sesión.');
        }

        // Devuelve los datos si el inicio de sesión es exitoso (ej. { token: '...' })
        return data;
    } catch (error) {
        console.error('Error en la función de login:', error);
        // Vuelve a lanzar el error para que el componente que llama pueda manejarlo.
        throw error;
    }
};

/**
 * Envía los datos de un nuevo usuario para registrarlo en el sistema.
 * @param {object} userData - Un objeto con los datos del nuevo usuario.
 * @param {string} userData.name - El nombre del usuario.
 * @param {string} userData.email - El email del usuario.
 * @param {string} userData.password - La contraseña del usuario.
 * @param {string} userData.password_confirmation - La confirmación de la contraseña.
 * @returns {Promise<object>} Una promesa que se resuelve con los datos del usuario creado.
 */
export const createUser = async (userData) => {
    // --- Validación de entrada ---
    if (!userData || typeof userData !== 'object') {
        throw new Error('Se requiere un objeto con los datos del usuario.');
    }
    if (!userData.name || typeof userData.name !== 'string' || userData.name.trim() === '') {
        throw new Error('El campo "name" es requerido y no puede estar vacío.');
    }
    if (!userData.email || typeof userData.email !== 'string' || userData.email.trim() === '') {
        throw new Error('El campo "email" es requerido y no puede estar vacío.');
    }
    if (!userData.password || typeof userData.password !== 'string' || userData.password.trim() === '') {
        throw new Error('El campo "password" es requerido y no puede estar vacío.');
    }
    // La API de Laravel no usa 'password_confirmation' directamente, pero es buena práctica enviarlo
    // si la validación del backend lo requiere (con la regla 'confirmed').
    // --- Fin de la validación ---

    try {
        const response = await fetch(`${API_BASE_URL}/create_user`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
            body: JSON.stringify(userData),
        });

        const data = await response.json();

        if (!response.ok) {
            // Si la respuesta no es exitosa (ej. 422 por validación), lanza un error.
            throw new Error(data.message || 'Error al crear el usuario.');
        }

        // Devuelve los datos del usuario creado.
        return data;
    } catch (error) {
        console.error('Error en la función createUser:', error);
        // Vuelve a lanzar el error para que el componente que llama pueda manejarlo.
        throw error;
    }
};