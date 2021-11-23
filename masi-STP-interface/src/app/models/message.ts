export class Message {

    author: string;
    context: any;
    message: string;
    response: string[];
    url: string;
    categories: string[][];
    ip: string;

    constructor() {
        this.ip = localStorage.getItem('CLIENT_IP');
    }
}
