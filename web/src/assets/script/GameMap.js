import { Base_Object } from "./BaseObject.js";

export class Game_Map extends Base_Object {
    constructor(ctx, parent) { //ctx为画布，parent为画布的父元素
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; //L为单位长度
        this.rows = 13;
        this.cols = 13;
    }

    start() {

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
        this.ctx.fillStyle = "green";
        this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
    }
}