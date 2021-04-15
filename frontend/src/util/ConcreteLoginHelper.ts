import LoginHelperStrategy from "./LoginHelperStrategy";

export default class ConcreteLoginHelper implements LoginHelperStrategy {
  login(params: Object): Boolean {
    return true;
  }
  logout(): Boolean {
    return false;
  }
}
