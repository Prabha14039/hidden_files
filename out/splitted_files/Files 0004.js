 game = document.getElementById("game");
    if (game == null) {
        throw new Error("No canvas with id `game` is found");
    }
    game.width = 800;
    game.height = 800;
    const ctx = game.getContext("2d");
    if (ctx == null) {
        throw new Error("2D context not supported");
    }
    let p2 = undefined;
    game.addEventListener("mousemove", (event) => {
        p2 = new Vector2(event.offsetX, event.offsetY).div(canvasSize(ctx)).mul(new Vector2(GRID_COL, GRID_ROWS));
        grid(ctx, p2);
    });
    grid(ctx, p2);
})();
