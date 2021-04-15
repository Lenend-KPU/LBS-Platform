import CheckLoginStrategy from "./CheckLoginStrategy";

export default class ConcreteCheckLogin implements CheckLoginStrategy {
  // 구현 TODO
  checkLogin() {
    return true;
  }
  checkError() {
    return {};
  }
}
