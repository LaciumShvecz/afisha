document.addEventListener('DOMContentLoaded', async function () {
    await showUserNameOnNavbar()
    await fillTableAboutUser();
});

async function dataAboutCurrentUser() {
    const response = await fetch("/api/profile")
    return await response.json();
}

async function fillTableAboutUser() {
    const currentUserTable = document.getElementById("currentUserTable");
    const currentUser = await dataAboutCurrentUser();

    let currentUserTableHTML = "";
    currentUserTableHTML +=
        `<tr>
            <td>${currentUser.name}</td>
            <td>${currentUser.username}</td>
            <td>${currentUser.age}</td>
            <td>${currentUser.password}</td>
            <td>${currentUser.roles.map(role => role.name.substring(5).concat(" ")).toString().replaceAll(",", "")}</td>
        </tr>`
    currentUserTable.innerHTML = currentUserTableHTML;
}

async function showUserNameOnNavbar() {
    const currentUserNavbar = document.getElementById("currentUserNavbar")
    const currentUser = await dataAboutCurrentUser();
    currentUserNavbar.innerHTML =
        `<strong>${currentUser.username}</strong>
                 with roles: 
                 ${currentUser.roles.map(role => role.name.substring(5).concat(" ")).toString().replaceAll(",", "")}`;
}