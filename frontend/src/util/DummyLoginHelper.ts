import LoginHelperStrategy from "./LoginHelperStrategy";

export default class DummyLoginHelper implements LoginHelperStrategy {
  login(params: Object): Boolean {
    return true;
  }
  logout(): Boolean {
    return false;
  }
}
