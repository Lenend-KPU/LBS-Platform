import DummyCheckLogin from "./DummyCheckLogin";

export default interface CheckLoginStrategy {
  // checkLogin 함수는 로그인 확인 side effect 후 로그인 여부를 리턴해야 함.
  checkLogin: () => Boolean;
}

export class checkLoginBuilder {
  static build(): CheckLoginStrategy {
    // 빌더 & 의존성 주입 패턴
    return new DummyCheckLogin();
  }
}
