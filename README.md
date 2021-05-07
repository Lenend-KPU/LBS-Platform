# KPU 2021 캡스톤디자인 - 컴퓨터공학과, IT경영학과

## Product
### 장소 데이터를 활용한 경험가치 공유 플랫폼
#### (Location Based Experience Sharing Service)
설명(TODO)

## Design
<img width="1082" alt="1" src="https://user-images.githubusercontent.com/22572874/117487409-051ab380-afa6-11eb-8e2e-70ff0e83a6e9.png">
<img width="1085" alt="2" src="https://user-images.githubusercontent.com/22572874/117487428-09df6780-afa6-11eb-9dd7-554d20aa468b.png">
<img width="1085" alt="3" src="https://user-images.githubusercontent.com/22572874/117487431-0b109480-afa6-11eb-88da-844ccbf05c01.png">
<img width="1086" alt="4" src="https://user-images.githubusercontent.com/22572874/117487433-0ba92b00-afa6-11eb-915d-728baff63daa.png">
<img width="1098" alt="5" src="https://user-images.githubusercontent.com/22572874/117487435-0c41c180-afa6-11eb-9480-5991076b47f2.png">
[링크 - Zeplin](https://scene.zeplin.io/project/608f9bd65948df337111c229)

## Installation
```
TODO
```

## Architecture
![Architecture](https://user-images.githubusercontent.com/22572874/113485131-13793980-94e7-11eb-82e6-d3941ca487db.png)

## Tech-Stack
|분류|기술|
|------|---|
|개발환경|Aws lightsail Ubuntu 20.04, Docker|
|DB|Redis, Postgresql(container)|
|Front-end|HTML5, CSS3, JavaScript, React Native, Expo|
|Back-end|Nginx, Django, Jenkins|
|api|Google maps, Mapbox, 상권 정보, 도로명주소|
|UI|Figma|

## Demo
GIF(TODO)

## Members
| 이름   | 학과         | 역할 | 소개 페이지                                         |
| ------ | ------------ | ---- | --------------------------------------------------- |
| 한승욱 | 컴퓨터공학과 | PM(PO), DevOps, Backend     | [개인 리포로 이동](https://github.com/SeungWookHan) |
| 박찬희 | 컴퓨터공학과 | Backend     |                                                   |
| 이재서 | IT경영학과   | Frontend     |                                                     |
| 주용진 | IT경영학과   |  DB    |                                                     |
| 김희구 | IT경영학과   |  UI    |                                                     |

## 데이터베이스 모델링
![디비](https://user-images.githubusercontent.com/22572874/115986401-f4a02b80-a5ea-11eb-9973-85481376ed39.png)


## API
### swagger or postman
- 사전 설계: [링크 - postman](https://documenter.getpostman.com/view/15074200/TzCJeUwy)
링크(TODO)

## PORTS
 | Name   | Port         | Description | 
| ------ | ------------ | ---- | 
| Nginx | 80, 443 | 설명 TODO  | 
| Django + Gunicorn| 8000 |  설명 TODO  | 
| Postgresql | 5432 | 설명 TODO    | 
| Jenkins | 8083, 50003 | 설명 TODO |

## Deploy
CI/CD 등 설명(TODO)

## Git Convention
### Process
- master/main 브랜치에서 각자 브랜치를 따서 작업한다.
- 본인의 작업이 완료되면 브랜치를 push한다.
- master/main 브랜치로 Pull Request를 작성한다.
```
1. git checkout master
2. git pull origin master --rebase
3. git branch feat/back/add-login-api
4. git checkout feat/back/add-login-api
- 본인의 작업 실시 -
- 완료 되었을 경우-
5. git add *
6. git commit -m "[feat/back] add-login-api - #2"
7. git push origin feat/back/add-login-api
- 이후 깃헙에 들어가서 PR 작성 및 리뷰 리퀘스트 -
8. 깃허브에서 pull request 생성
9. 리뷰어의 코드 리뷰
10. pull request merge
```
- 리뷰어의 Code Review를 받고 Pull Request를 Merger한다.
**아래 상세 규칙 참조**

### Branch
#### {타입}/{역할}/{내용}
- 브랜치명의 경우에는 아래 Commit Message에서 설명하는 **타입**, **종류**의 형식을 활용한다.
- **내용**의 경우에는 **-**으로 구분하며 개조식으로 작성한다.
- 각각 **/** 로 구분한다.
- ex) feat/front/add-react-app
- ex) feat/back/add-login-api

### Commit Message
1. 먼저 커밋 메시지는 크게 **제목, 본문** 두 가지 파트로 나누고, 각 파트는 빈줄을 두어서 구분합니다.
2. 커밋 메세지는 모두 **한글**로 통일합니다.
3. 제목의 경우 타입, 역할은 **대괄호 안에 소문자로 작성**합니다.
4. type 다음에는 **/ 로 분리**하여 **역할을 명시**한다.
5. 제목 작성후 우측에 **#이슈번호**를 남겨서 어떤 이슈에 대한 작업인지 명시합니다.
6. ex) #> git commit -m "[fix/front] XSS Vulnerability - #20"
```
[type/{front or back or AI or RPI}] Subject - #2 // -> 제목 

(한 줄을 띄워 분리합니다.)

body //  -> 본문 
```
---
### 타입
- 어떤 의도인지 타입에 명세한다.
1. feat: 새로운 기능 추가
```
ex)
[feat] 버튼 클릭 시 날짜 선택 하는 기능 추가

body: 버튼 클릭 시 picker를 통해 날짜를 선택하게 구현
picker뷰는 toolbar를 이용했음
```
2. fix: 버그 수정
```
ex)
[fix] 라벨 길이가 짤리는 버그 수정

body: 라벨 길이를 view leading에서 간격 추가
```
3. refactor: 코드 리팩토링
```
ex)
[refactor] MainVC 코드 정리

body: convension 내용 중 변수명을 지키지 못한 점 수정
lowerCamelCase를 지켜서 변수명을 수정했음
```
4. docs: 문서 수정하는 경우
```
ex)
[docs] README.md 파일 수정

body: Git Message Convention 방법 정리
```

### 역할
- front: 프론트엔드 관련 작업
- back: 백엔드 관련 작업
- db: 데이터베이스 관련 작업
- etc

### body
- **"body: " 를 앞에 포함**하여 작성합니다.
- 긴 설명이 필요한 경우에 작성합니다.
- **어떻게** 했는지가 아니라, **무엇을** **왜** 했는지를 작성합니다.
- 최대 75자를 넘기지 않도록 합니다.

## Communication

*** 프로젝트 진행은 비대면을 원칙으로 제한적 대면으로 진행 ***

### Tools: Slack, Trello, Google Drive, Google Meets, Zoom, Github

##### Slack(슬랙)

- **프로젝트 관련 모든 소통은 슬랙을 통해서 진행**
- **announcement**: 전체 공지(회식, 외부 미팅 등) 채널
- **announcement-컴퓨터공학과**: 컴퓨터공학과 관련 공지 채널
- **announcement-it경영학과**: it경영학과 관련 공지 채널
- **chat-asdf**: 자유롭게 대화를 할 수 있는 채널
- **general**: 회의 링크를 공유하기 위한 채널
- **ideation**: 아이디어 회의를 위한 채널, 여기서 선정된 아이디어는 market-research 단계로 감
- **market-research**: 선정된 아이디어의 구현 가능성, 중요성, 필요 기술 등을 조사하는 채널
- **product-design**: 디자인 관련 진행상황 등을 공유하기 위한 채널
- **product-dev**: 개발 관련 진행사항 등을 공유하기 위한 채널
- **session**: 팀원간 zoom 등을 통해 온라인 세션 등을 진행할때 활용하는 채널
- **stand-up**: 매일 Dixi app을 통해 자신이 무엇을 했고, 무엇을 할 예정이고, 어떠한 문제점이 있는지 간단하게 공유하기 위한 채널

##### Trello(트렐로)

- 칸반 방식으로 프로젝트를 관리하고 각 기능별 일정 및 진행사항 공유를 하기 위함
- 칸반 템플릿을 사용하고 이 중 To Do, Doing, Testing, Done 리스트를 중점으로 사용
- **To Do**: 자신이 해야하거나 할 예정의 업무들을 카드로 제작 - 업무명, 업무 종류, 중요도, 참여자 필수
- **Doing**: To Do에서 업무를 시작한 카드는 Doing으로 옮김, 특이사항 발생시 카드에 표시
- **Testing**: 개발 기능의 경우 서버 배포를 위한 테스트, 디자인의 경우 export 등 테스트 할때 카드를 Doing에서 Testing으로 옮김
- **Done**: 완료한 모든 업무는 Done으로 옮김

##### Google Drive(구글드라이브)

- word, excel, ppt 등의 산출물은 전부 구글드라이브에 저장
- **Google Docs**: 문서 자료는 최대한 드라이브 내에서 구글독스로 생성 및 작성
- **Google SpreadSheet**: 엑셀 자료는 최대한 드라이브 내에서 구글스프레드시트로 생성 및 작성
- **Google Presentation**: 시각화 자료는 최대한 드라이브 내에서 구글프레젠테이션으로 생성 및 작성
- **Google Form**: 설문조사가 필요할 시 해당 폼은 최대한 드라이브 내에서 구글폼으로 생성 및 작성

##### Google Meets(구글 미츠)

- 온라인 회의를 진행할때 사용

##### Zoom(줌)

- 온라인 세션(팀원들끼리 지식 공유, 자신의 업무 실시간 공유 등)을 진행할 때 사용

##### Githun(깃허브)

- Git으로 코드 버전 관리를 저장하기 위함
- Feature Branch 방식을 지향

