## Spring Batch, Spring Boot Batch
``Spring Batch`` : 단발성으로 대용량의 데이터를 처리하는 어플리케이션이다.<br>
``Spring Boot Batch`` : Spring Batch의 설정 요소들을 간편화하여 Spring Batch를 빠르게 설정하도록 도움을 준다.

<h3>장점</h3>
<ul>
  <li>대용량 데이터 처리에 최적화되어 고성능이다.</li>
  <li>효과적인 로깅, 통계 처리, 트랜잭션 관리 등 재사용 가능한 필수 기능을 지원한다.</li>
  <li>수동으로 처리하지 않도록 자동화되어 있다.</li>
  <li>예외사항과 비정상 동작에 대한 방어 기능이 있다.</li>
  <li>Spring Boot Batch는 반복적인 작업 프로세스를 이해하면 비지니스 로직에 집중할 수 있다.</li>
</ul>
<h3>주의사항</h3>
Spring Boot Batch는 Spring Boot를 간편하게 사용 할 수 있게 래핑한 프로젝이다. 따라서 Spring Boot Batch와 Spring Batch 모두 다음과 같은 주의사항을 염두해야 한다.<br><br>
<ul>
  <li>가능하면 단순화해서 복잡한 구조와 로직을 피한다.</li>
  <li>데이터를 직접 사용하는 편이 빈번하게 일어나므로 데이터 무결성을 유지하는데 유효성 검사 등의 방어책이 있어야한다.</li>
  <li>배치 처리 시스템 I/O 사용을 최소화해야 한다. 잦은 I/O로 데이터베이스 컨넥션과 네트워크 비용이 커지면 성능에 영향을 줄 수 있기 때문이며, 가능하면 한번에 데이터를 조회하여 메모리에 저장해두고 처리를 한 다음에 그 결과를 한번에 데이터베이스에 저장하는것이 좋다.</li>
  <li>일반적으로 같은 서비스에 사용되는 웹 API, 배치, 기타 프로젝트들은 서로 영향을 준다. 따라서 배치 처리가 진행되는 동안 다른 프로젝트 요소에 영향을 주는 경우가 없는지 주의를 기울여야 한다.</li>
  <li>Spring Boot는 배치 스케쥴러를 제공하지 않는다. 따라서 배치 처리 기능만 제공하여 스케쥴링 기능은 스프링에서 제공하는 쿼치 프레임워크 등을 이용해야 한다. 리눅스 crontab 명령은 가장 간단히 사용 할 수 있지만 이는 추천하지 않는다. crontab의 경우 각 서버마다 따로 스케쥴러를 관리해야 하며 무엇보다 클러스터링 기능이 제공되지 않는다. 반면에 쿼츠 같은 스케쥴링은 프레임워크를 사용한다면 클러스터링뿐만 아니라 다양한 스케쥴링 기능, 실행 이력 관리 등 여러 이점을 얻을 수 있다.</li>
</ul>

## Spring Boot Batch 이해
일반적인 시나리오는 다음과 같은 3단계로 이루어진다.

<ul>
  <li>읽기(Read) : 데이터 저장소(일반적으로 데이터베이스)에서 특정 데이터 레코드를 읽는다.</li>
  <li>처리(Processing) : 원하는 방식으로 데이터 가공/처리 한다.</li>
  <li>쓰기(Write) : 수정된 데이터를 다시 저장소(데이터베이스)에 저장한다.</li>
</ul>
<img src="https://user-images.githubusercontent.com/47962660/66260270-5cb7d280-e7f7-11e9-815b-2292e1837b61.png"/>
