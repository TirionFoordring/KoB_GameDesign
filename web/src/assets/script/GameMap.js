import { Base_Object } from "./BaseObject.js";
import { Wall } from "./Wall.js";

export class Game_Map extends Base_Object {
    constructor(ctx, parent) { //ctx为画布，parent为画布的父元素
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //L为单位长度
        this.rows = 13;
        this.cols = 13;
        this.walls = [];
    }

    Creat_Wall() {
        let g = [];
        for (let r = 0; r < this.rows; r++) {
            g[r] = [];
            for (let c = 0; c < this.cols; c++) {
                g[r][c] = false;
            }
        }

        //给四周添加墙壁
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (r === 0 || c === 0 || r === this.rows - 1 || c === this.cols - 1) {
                    g[r][c] = true;
                }
            }
        }

        //draw all walls
        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
    }

    start() {
        this.Creat_Wall();
    }

    update_size() {
        //clientWidth和clientHeight表示求div的长和宽
        this.L = Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows);
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
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
                this.ctx.fillRect(c * this.L, r * this.L, this.L + 1, this.L + 1);
            }
        }
    }
}