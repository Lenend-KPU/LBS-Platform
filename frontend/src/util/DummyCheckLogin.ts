import CheckLoginStrategy from "./CheckLoginStrategy";

export default class DummyCheckLogin implements CheckLoginStrategy {
  checkLogin() {
    return true;
  }
}
