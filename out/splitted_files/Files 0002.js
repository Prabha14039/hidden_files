"use strict";
class Vector2 {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
    distanceTo(that) {
        const dx = this.x - that.x;
        const dy = this.y - that.y;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
    sub(that) {
        return new Vector2(this.x - that.x, this.y - that.y);
    }
    add(that) {
        return new Vector2(this.x + that.x, this.y + that.y);
    }
    div(that) {
        return new Vector2(this.x / that.x, this.y / that.y);
    }
    mul(that) {
        return new Vector2(this.x * that.x, this.y * that.y);
    }
    length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    norm() {
        const l = this.length();
        if (l !== 0) {
            return new Vector2(this.x / l, this.y / l);
        }
        return new Vector2(0, 0);
    }
    scale(value) {
        return new Vector2(this.x * value, this.y * value);
    }
    array() {
        return [this.x, this.y];
    }
}
const GRID_ROWS = 10;
const GRID_COL = 10;
const GRID_SIZE = new Vector2(GRID_COL, GRID_ROWS);
let scene = Array(GRID_ROWS).fill(0).map(() => Array(GRID_COL).fill(0));
function fillCircle(ctx, center, radius) {
    ctx.beginPath();
    ctx.arc(...center.array(), radius, 0, 2 * Math.PI);
    ctx.fill();
}
function strokeLine(ctx, p1, p2) {
    ctx.beginPath();
    ctx.moveTo(...p1.array());
    ctx.lineTo(...p2.array());
    ctx.stroke();
}
function canvasSize(ctx) {
    return new Vector2(ctx.canvas.width, ctx.canvas.height);
}
function snap(x, dx) {
    const eps = 1e-3;
    if (dx > 0)
        return Math.ceil(x + Math.sign(dx) * eps);
    if (dx < 0)
        return Math.floor(x + Math.sign(dx) * eps);
    return x;
}
function hitting_cell(p1, p2) {
    const eps = 1e-3;
    const d = p2.sub(p1);
    return new Vector2(Math.floor(p2.x + Math.sign(d.x) * eps), Math.floor(p2.y + Math.sign(d.y) * eps));
}
function rayStep(p1, p2) {
    // y= mx+c
    // y1=mx1+c
    // y2=mx2+c
    let p3 = p2;
    const d = p2.sub(p1);
    if (d.x != 0) {
        const m = d.y / d.x;
        const c = p1.y - m * p1.x;
        {
            const x3 = snap(p2.x, d.x);
            const y3 = x3 * m + c;
            //fillCircle(ctx,new Vector2(x3,y3),0.1);
            p3 = new Vector2(x3, y3);
        }
        if (m !== 0) {
            const y3 = snap(p2.y, d.y);
            const x3 = (y3 - c) / m;
            //fillCircle(ctx,new Vector2(x3,y3),0.1);
            const p4 = new Vector2(x3, y3);
            if (p2.distanceTo(p4) < p2.distanceTo(p3)) {
                p3 = p4;
            }
        }
    }
    else {
        const y3 = snap(p2.y, d.y);
        const x3 = p2.x;
        p3 = new Vector2(x3, y3);
    }
    return p3;
}
function grid(ctx, p2) {
    ctx.reset();
    ctx.fillStyle = "#181818";
    ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    ctx.strokeStyle = "#303030";
    ctx.scale(ctx.canvas.width / GRID_COL, ctx.canvas.height / GRID_ROWS);
    ctx.lineWidth = 0.03;
    for (let y = 0; y < GRID_ROWS; y++) {
        for (let x = 0; x < GRID_COL; x++) {
            if (scene[y][x] !== 0) {
                ctx.fillStyle = "#303030";
                ctx.fillRect(x, y, 1, 1);
            }
        }
    }
    for (let x = 0; x <= GRID_COL; ++x) {
        strokeLine(ctx, new Vector2(x, 0), new Vector2(x, GRID_ROWS));
    }
    for (let y = 0; y <= GRID_ROWS; ++y) {
        strokeLine(ctx, new Vector2(0, y), new Vector2(GRID_COL, y));
    }
    let p1 = new Vector2(GRID_COL * 0.56, GRID_ROWS * 0.33);
    ctx.fillStyle = "magenta";
    fillCircle(ctx, p1, 0.2);
    if (p2 !== undefined) {
        for (;;) {
            fillCircle(ctx, p2, 0.2);
            ctx.strokeStyle = "magenta";
            strokeLine(ctx, p1, p2);
            const c = hitting_cell(p1, p2);
            if (c.x < 0 || c.x >= GRID_SIZE.x || c.y < 0 || c.y >= GRID_SIZE.y || scene[c.y][c.x] == 1) {
                break;
            }
            const p3 = rayStep(p1, p2);
            p1 = p2;
            p2 = p3;
        }
    }
}
(() => {
    scene[1][1] = 1;
    const