import { DEBUG } from "../../Settings";
import ConcreteCheckLogin from "./ConcreteCheckLogin";
import ConcreteLoginHelper from "./ConcreteLoginHelper";
import DummyCheckLogin from "./DummyCheckLogin";
import DummyLoginHelper from "./DummyLoginHelper";

export default interface LoginHelperStrategy {
  // login 함수는 params 객체를 받아서 로그인 처리 후 성공/실패 boolean을 반환해야 함.
  login: (params: Object) => Boolean;
  logout: () => Boolean;
}

export class LoginHelperBuilder {
  static build(): LoginHelperStrategy {
    // 빌더 & 의존성 주입 패턴
    return DEBUG ? new DummyLoginHelper() : new ConcreteLoginHelper();
  }
}
