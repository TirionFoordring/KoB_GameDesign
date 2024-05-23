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

        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 2; //左下角的蛇开局时眼睛朝上，右上角的蛇开局时眼睛朝下
        //蛇的眼睛不同方向x和y的偏移量
        this.eye_dx = [[-1, 1], [1, 1], [1, -1], [-1, -1]];
        this.eye_dy = [[-1, -1], [-1, 1], [1, 1], [-1, 1]];

    }

    start() {

    }

    set_direction(d) {
        this.direction = d;
    }

    check_tail_increasing() {

        //the length of snakes increase 1 in each of the first 10 rounds
        if (this.step <= 10) return true;
        if (this.step % 3 === 1) return true;

        return false;

    }

    //将蛇的状态变为走下一步
    next_step() {

        const d = this.direction;
        this.next_cell = new cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.eye_direction = d;
        this.direction = -1; //移动完后清空移动指令
        this.status = "move";
        this.step++;

        const k = this.cells.length;
        for (let i = k; i > 0; i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }

        if (!this.gamemap.check_valid(this.next_cell)) {
            this.status = "die";
        }

    }

    update_move() {

        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy); // 2024-05-20, 更改了此处的错误，将"+"写错成","导致蛇左右移动时有平滑移动的动画，上下移动的时候却没有动画

        if (distance < this.eps) {

            this.cells[0] = this.next_cell; //将新的点添加为蛇头
            this.next_cell = null;
            this.status = "idle"; //状态设为静止

            //if the length of the snake doesn't increase, delate the tail of the snake
            if (!this.check_tail_increasing()) {
                this.cells.pop();
            }

        } else {

            const move_distance = this.speed * this.timedelta / 1000; //每两帧之间走过的距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if (!this.check_tail_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1], tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }

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

        if (this.status === "die") {
            ctx.fillStyle = "white";
        }

        //将蛇的身体画为圆
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, Math.PI * 2); //从左至右5个参数分别为：圆心横、纵坐标、半径、圆弧的起始角度、终止角度，半径乘以0.8的目的是使蛇变瘦
            ctx.fill()
        }

        //把蛇填充丰满：枚举每相邻两个格子填充上正方形
        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i - 1], b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps)
                continue;
            else if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }

        //draw the eyes
        ctx.fillStyle = "black";
        for (let i = 0; i < 2; i++) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.13) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.13) * L;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.07, 0, Math.PI * 2);
            ctx.fill();
        }
    }
}