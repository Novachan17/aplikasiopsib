function toggleSidebar() {
    const sidebar = document.querySelector('.sidebar');
    const main = document.querySelector('.main-content');

    sidebar.classList.toggle('collapsed');

    // HANYA desktop (>= 992px)
    if (window.innerWidth >= 992) {
        main.classList.toggle('collapsed');
    }
}

// AUTO SEARCH
document.addEventListener("DOMContentLoaded", function () {
    const input = document.getElementById("search");
    if (!input) return;

    let timer = null;
    input.addEventListener("keyup", function () {
        clearTimeout(timer);
        timer = setTimeout(() => {
            input.form.submit();
        }, 500);
    });
});



    // FILTER GEJALA
    document.addEventListener("keyup", function (e) {
        if (e.target.id === "searchGejala") {
            const keyword = e.target.value.toLowerCase();
            document.querySelectorAll(".gejala-card").forEach(card => {
                card.style.display = card.innerText.toLowerCase().includes(keyword)
                    ? "block" : "none";
            });
        }
    });

    // AKTIFKAN BUTTON
    document.addEventListener("change", function (e) {
        if (e.target.name === "gejalaIds") {
            const btn = document.getElementById("btnProses");
            if (!btn) return; // halaman lain

            const checked = document.querySelectorAll(
                "input[name='gejalaIds']:checked"
            ).length > 0;

            btn.disabled = !checked;
        }
    });


