import { DEBUG } from "../../Settings";
import ConcreteCheckLogin from "./ConcreteCheckLogin";
import DummyCheckLogin from "./DummyCheckLogin";

export default interface CheckLoginStrategy {
  // checkLogin 함수는 로그인 확인 side effect 후 로그인 여부를 리턴해야 함.
  checkLogin: () => Boolean;
  // checkError 함수는 로그인중 에러가 있다면 에러 객체를 리턴, 그렇지 않으면 빈 객체를 리턴해야 함.
  checkError: () => IError | {};
}

export interface IError {
  status: number;
}

export class CheckLoginBuilder {
  static build(): CheckLoginStrategy {
    // 빌더 & 의존성 주입 패턴
    return DEBUG ? new DummyCheckLogin() : new ConcreteCheckLogin();
  }
}
