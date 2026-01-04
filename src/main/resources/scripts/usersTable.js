async function dataAboutAllUsers() {
    const response = await fetch("/api/admin/users");
    return await response.json();
}

async function dataAboutCurrentUser() {
    const response = await fetch("/api/profile")
    return await response.json();
}


async function fillTableOfAllUsers() {
    const usersTable = document.getElementById("usersTableId");
    const users = await dataAboutAllUsers();


    let usersTableHTML = "";
    for (let user of users) {
        usersTableHTML +=
            `<tr>
                <td>${user.name}</td>
                <td>${user.username}</td>
                <td>${user.age}</td>
                <td>${user.password}</td>
               <td>${user.roles.map(role => role.name.substring(5).concat(" ")).toString().replaceAll(",", "")}</td>
                <td>
                    <button class="btn btn-info"
                            data-bs-toggle="modal"
                            data-bs-target="#editModal"
                            data-user-id="${user.id}">
                        Edit</button>
                </td>
                <td>
                    <button class="btn btn-danger"
                            data-bs-toggle="modal"
                            data-bs-target="#deleteModal"
                            data-user-id="${user.id}">                     
                        Delete</button>
                </td>
            </tr>`;
    }
    usersTable.innerHTML = usersTableHTML;
}