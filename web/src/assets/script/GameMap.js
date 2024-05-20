import { Base_Object } from "./BaseObject.js";
import { snake } from "./Snake.js";
import { Wall } from "./Wall.js";

export class Game_Map extends Base_Object {
    constructor(ctx, parent) { //ctx为画布，parent为画布的父元素
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //L为单位长度
        this.rows = 13;
        this.cols = 14;
        this.walls = [];
        this.inner_walls_number = 20;

        this.snakes = [
            new snake({ id: 0, color: "#4876EC", r: this.rows - 2, c: 1 }, this),
            new snake({ id: 1, color: "#F94848", r: 1, c: this.cols - 2 }, this)
        ]
    }

    //Ensure that the bottom left and top right corners of the map are connected
    //source:(sx, sy); target:(tx, ty)
    connectivity(g, sx, sy, tx, ty) {
        // if source == target, that means this function is over;
        if (sx === tx && sy === ty) return true;

        g[sx][sy] = true;
        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for (let i = 0; i < 4; i++) {
            let x = sx + dx[i], y = sy + dy[i];
            if (!g[x][y] && this.connectivity(g, x, y, tx, ty))
                return true
        }

        return false;
    }

    Create_Wall() {
        const g = [];
        for (let r = 0; r < this.rows; r++) {
            g[r] = [];
            for (let c = 0; c < this.cols; c++) {
                g[r][c] = false;
            }
        }

        //add walls around the map
        for (let r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = true;
        }

        for (let c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = true;
        }

        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        //create inner walls randomly
        for (let i = 0; i < this.inner_walls_number / 2; i++) {
            for (let j = 0; j < 1000; j++) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);

                //如果此处已经是墙壁了，再次搜索
                if (g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue;

                //左下角和右上角禁止生成墙壁
                if ((r === this.rows - 2 && c === 1) || (r === 1 && c === this.cols - 2)) continue;

                //add the wall symmetrically
                g[r][c] = true;
                g[this.rows - 1 - r][this.cols - 1 - c] = true;
                break;

            }
        }

        //copy the current map and deliver it to the function "connectivity" in order to check the connectivity of this map.
        //把g转换为JSON文件再解析，确保复制的同时不会影响原本的文件
        const copy_g = JSON.parse(JSON.stringify(g));

        //check whether the the bottom left and top right corners of the map are connected
        if (!this.connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)) return false; //2024-05-20更改，参数传入错误导致地图的长和宽一旦变得不同就无法生成地图

        //draw all walls
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        return true;
    }

    //获取用户输入
    add_listening_events() {
        this.ctx.canvas.focus();

        const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            if (e.key === "w") snake0.set_direction(0);
            else if (e.key === "d") snake0.set_direction(1);
            else if (e.key === "s") snake0.set_direction(2);
            else if (e.key === "a") snake0.set_direction(3);
            else if (e.key === "ArrowUp") snake1.set_direction(0);
            else if (e.key === "ArrowRight") snake1.set_direction(1);
            else if (e.key === "ArrowDown") snake1.set_direction(2);
            else if (e.key === "ArrowLeft") snake1.set_direction(3);
        })
    }

    start() {
        for (let i = 0; i < 1000; i++) {
            if (this.Create_Wall())
                break;
        }

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