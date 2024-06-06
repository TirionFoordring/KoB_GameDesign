import { Base_Object } from "./BaseObject.js";
import { snake } from "./Snake.js";
import { Wall } from "./Wall.js";

export class Game_Map extends Base_Object {
    constructor(ctx, parent, store) { //ctx为画布，parent为画布的父元素
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //L为单位长度
        this.store = store;
        this.rows = 13;
        this.cols = 14;
        this.walls = [];
        this.inner_walls_number = 20;

        this.snakes = [
            new snake({ id: 0, color: "#4876EC", r: this.rows - 2, c: 1 }, this),
            new snake({ id: 1, color: "#F94848", r: 1, c: this.cols - 2 }, this)
        ]
    }

    Create_Wall() {
        const g = this.store.state.pk.gamemap;

        //draw all walls
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    //获取用户输入
    add_listening_events() {
        this.ctx.canvas.focus();
        this.ctx.canvas.addEventListener("keydown", e => {
            let d = -1;
            if (e.key === "w") d = 0;
            else if (e.key === "d") d = 1;
            else if (e.key === "s") d = 2;
            else if (e.key === "a") d = 3;

            //若进行了合法的移动操作，则向后端传入消息
            if (d >= 0) {
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d,
                }));
            }
        })
    }

    start() {
        this.Create_Wall();
        this.add_listening_events();
    }

    update_size() {
        //clientWidth和clientHeight表示求div的长和宽
        //对L执行parseInt()取整，防止格子之间出现缝隙
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    //check are the two snakes are ready.
    check_ready() {
        for (const snake of this.snakes) {
            if (snake.status !== "idle") return false;
            if (snake.direction === -1) return false;
        }
        return true;
    }

    next_step() {
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }

    //检测下一步不能碰撞墙或者身体
    check_valid(cell) {

        for (const wall of this.walls) {
            if (wall.r === cell.r && wall.c === cell.c)
                return false;
        }

        for (const snake of this.snakes) {
            let k = snake.cells.length;
            if (!snake.check_tail_increasing()) k--; //当蛇尾下一步会前进的时候，下一格是可以走的
            for (let i = 0; i < k; i++) {
                if (snake.cells[i].r === cell.r && snake.cells[i].c === cell.c)
                    return false;
            }
        }

        return true;

    }

    update() {
        this.update_size();
        if (this.check_ready()) {
            this.next_step();
        }
        this.render();
    }

    //render为渲染
    render() {
        const color_even = "#AAD751", color_odd = "#A2D149";
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if ((r + c) % 2 === 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
        }
    }
}