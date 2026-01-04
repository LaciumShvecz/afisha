document.addEventListener('DOMContentLoaded', async function () {
    await showUserNameOnNavbar()
    await fillTableOfAllUsers();
    await addNewUserForm();
    await DeleteModalHandler();
    await EditModalHandler();
});

async function showUserNameOnNavbar() {
    const currentUserNavbar = document.getElementById("currentUserNavbar")
    const currentUser = await dataAboutCurrentUser();
    currentUserNavbar.innerHTML =
        `<strong>${currentUser.username}</strong>
                 with roles: 
                 ${currentUser.roles.map(role => role.name.substring(5).concat(" ")).toString().replaceAll(",", "")}`;
}