import { Base_Object } from "./BaseObject.js";
import { cell } from "./Cell.js";

export class snake extends Base_Object {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells = [new cell(info.r, info.c)]; //cells数组存放蛇的身体，cells[0]存放蛇头
        this.next_cell = null; //下一步的目标位置

        this.speed = 5; //蛇每秒钟走5个格子
        this.status = "idle"; //蛇的当前状态，idle表示精致，move表示正在移动，die表示死亡
        this.direction = -1; //direction为下一步的指令，-1表示没有指令，0 1 2 3对应上 右 下 左
        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];
        this.step = 0; //蛇的回合数，用来判断是否需要变长
        this.eps = 1e-2; //eps表示误差
    }

    start() {

    }

    set_direction(d) {
        this.direction = d;
    }

    //将蛇的状态变为走下一步
    next_step() {
        const d = this.direction;
        this.next_cell = new cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1; //移动完后清空移动指令
        this.status = "move";
        this.step++;

        const k = this.cells.length;
        for (let i = k; i > 0; i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
    }

    update_move() {
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx, dy * dy);

        if (distance < this.eps) {
            this.cells[0] = this.next_cell; //将新的点添加为蛇头
            this.next_cell = null;
            this.status = "idle"; //状态设为静止

        } else {
            const move_distance = this.speed * this.timedelta / 1000; //每两帧之间走过的距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;
        }
    }

    update() {
        if (this.status === "move") {
            this.update_move();
        }
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color; //fillStyle用于指定颜色

        //将蛇的身体画为圆
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2, 0, Math.PI * 2); //从左至右5个参数分别为：圆心横、纵坐标、半径、圆弧的起始角度、终止角度
            ctx.fill()
        }

    }
}