import CheckLoginStrategy from "./CheckLoginStrategy";
import LoginHelperStrategy from "./LoginHelperStrategy";

export default class DummyCheckLogin
  implements CheckLoginStrategy, LoginHelperStrategy {
  static status: Boolean = true;
  checkLogin() {
    return DummyCheckLogin.status;
  }
  checkError() {
    return {};
  }
  login() {
    DummyCheckLogin.status = true;
    return true;
  }
  logout() {
    DummyCheckLogin.status = false;
    return true;
  }
}
