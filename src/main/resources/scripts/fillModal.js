async function getUserDataById(id) {
    const response = await fetch(`/api/admin/users/${id}`);
    return await response.json();
}

async function fillModal(modal) {

    modal.addEventListener("show.bs.modal", async function (event) {
        console.log(event.relatedTarget);

        const userId = event.relatedTarget.dataset.userId;
        console.log("Fetching user data for id:", userId);
        const user = await getUserDataById(userId);

        const modalBody = modal.querySelector(".modal-body");


        const nameInput = modalBody.querySelector("input[data-user-id='name']");
        const usernameInput = modalBody.querySelector("input[data-user-id='username']");
        const ageInput = modalBody.querySelector("input[data-user-id='age']");
        const passwordInput = modalBody.querySelector("input[data-user-id='password']");
        if (passwordInput !== null) {
            passwordInput.value = user.password;
        }


        nameInput.value = user.name;
        usernameInput.value = user.username;
        ageInput.value = user.age;


        let rolesSelect = HTMLSelectElement;

        let rolesSelectDelete = modalBody.querySelector("select[data-user-id='allRolesDelete']");
        let rolesSelectEdit = modalBody.querySelector("select[data-user-id='allRolesEdit']");
        let userRolesHTML = "";

        if (rolesSelectDelete !== null) {
            rolesSelect = rolesSelectDelete;
            for (let i = 0; i < user.roles.length; i++) {
                userRolesHTML +=
                    `<option value="${user.roles[i].name}">${user.roles[i].name
                        .substring(5).concat(" ").toString().replaceAll(",", "")}</option>`;
            }
        } else if (rolesSelectEdit !== null) {
            rolesSelect = rolesSelectEdit;
            userRolesHTML +=
                `<option value="ROLE_USER">USER</option>
                 <option value="ROLE_ADMIN">ADMIN</option>`
        }

        rolesSelect.innerHTML = userRolesHTML;
    })
}