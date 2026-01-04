async function getRoles() {
    const response = await fetch("/api/admin/roles");
    return await response.json();
}

async function createNewUser(user) {
    const response = await fetch("/api/admin/users/", {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(user)
    });

    if (!response.ok) {
        const errorData = await response.json();
        return {success: false, errors: errorData};
    }

    return {success: true};
}

async function addNewUserForm() {
    const newUserForm = document.getElementById("saveNewUser");

    newUserForm.addEventListener('submit', async function (event) {
        event.preventDefault();

        const name = newUserForm.querySelector("#nameNew").value.trim();
        const username = newUserForm.querySelector("#usernameNew").value.trim();
        const age = newUserForm.querySelector("#ageNew").value.trim();
        const password = newUserForm.querySelector("#passwordNew").value.trim();

        const rolesSelected = document.getElementById("rolesNew");

        let allRoles = await getRoles();
        let AllRoles = {};
        for (let role of allRoles) {
            AllRoles[role.name] = role.id;
        }
        let roles = [];
        for (let option of rolesSelected.selectedOptions) {
            if (Object.keys(AllRoles).indexOf(option.value) !== -1) {
                roles.push({roleId: AllRoles[option.value], name: option.value});
            }
        }

        const newUserData = {
            name: name,
            username: username,
            age: age,
            password: password,
            roles: roles
        };

        const result = await createNewUser(newUserData);

        if (result.success) {
            // Очистка формы и текста ошибок
            newUserForm.reset();
            displayValidationErrors({}); // Очистка ошибок

            // Переход на вкладку с таблицей пользователей и обновление таблицы
            document.querySelector('#admin-users-table-tab').click();
            await fillTableOfAllUsers();
        } else {
            displayValidationErrors(result.errors);
        }
    });
}

function displayValidationErrors(errors) {
    document.getElementById("nameError").textContent = errors.name || '';
    document.getElementById("usernameError").textContent = errors.username || '';
    document.getElementById("ageError").textContent = errors.age || '';
    document.getElementById("passwordError").textContent = errors.password || '';
}