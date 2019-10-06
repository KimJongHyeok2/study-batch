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

<h3>Job</h3>
<ul>
  <li>배치 처리 과정을 하나의 단위로 만들어 표현한 객체이며 전체 배치 처리에 있어 항상 최상단 계층에 있다.</li>
  <li>위에서 하나의 Job(일감) 안에는 여러 Step(단계)이 있다고 설명했던 바와 같이 Spring Batch에서 Job 객체는 여러 Step 인스턴스를 포함하는 컨테이너이다.</li>
  <li>Job 객체를 만드는 빌더는 여러 개 있다. 여러 빌더를 통합하여 처리하는 공장인 JobBuilderFactory로 원하는 Jdb을 쉽게 만들 수 있다.</li>
  <li>JobBuilderFactory는 JobBuilder를 생성할 수 있는 get() 메소드를 포함하고 있다. get() 메소드는 새로운 JobBuider를 생성해서 반환한다.</li>
  <li>JobBuilderFactory에서는 생성되는 모든 JobBuilder가 Repository를 사용한다.</li>
  <li>JobBuilder는 직접적으로 Job을 생성하는 것이 아니라 별도의 구체적 빌더를 생성하여 변환하고 경우에 따라 Job 생성 방법이 모두 다를 수 있다는 점을 유연하게 처리할 수 있다.</li>
</ul>
<h3>JobInstance</h3>
<ul>
  <li>배치 처리에서 Job이 실행될 때 하나의 Job 실행 단위이다. 만약, 하루 한번씩 배치의 Job이 실행된다면 어제와 오늘 실행한 각각의 Job을 JobInstance라 부를 수 있다.</li>
  <li>각각의 JobInstance는 하나의 JobExecution을 갖는 것이 아니다. 오늘 Job을 실행했는데 실패했다면 다음날 동일한 JobInstance를 가지고 또 실행한다.</li>
  <li>Job의 실행을 실패하면 JobInstance가 끝난것으로 간주되지 않는다. 그렇기 때문에 JobInstance는 어제 실패한 JobExecution과 오늘 성공한 JobExecution 두 개를 가지게 된다. 즉, JobExecution은 여러 개를 가질 수 있다.</li>
</ul>
<h3>JobExecution</h3>
<ul>
  <li>JobInstance에 대한 한 번의 실행을 나타내는 객체이다.</li>
  <li>만약 오늘 Job의 실행을 실패하여 내일 다시 동일한 Job을 실행하면 오늘/내일 모두 같은 JobInstance를 사용한다.</li>
  <li>실제로 JobExecution 인터페이스를 보면 Job 실행에 대한 정보를 담고 있는 도메인 객체가 있다. JobExecution은 JobInstance, 배치 실행 상태, 시작 시간, 끝난 시간, 실패했을 경우의 메시지 등의 정보를 담고 있다.</li>
</ul>
<h3>JobParameters</h3>
<ul>
  <li>Job이 실행될 때 필요한 파라미터들을 Map 타입으로 지정하는 객체이다.</li>
  <li>JobInstance를 구분하는 기준이 되기도 한다.</li>
  <li>JobInstance와 1:1 관계이다.</li>
</ul>
<h3>Step</h3>
<ul>
  <li>실질적인 배치 처리를 정의하고 제어하는데 필요한 모든 정보가 있는 도메인 객체이며 Job을 처리하는 실질적인 단위로 쓰인다.</li>
  <li>모든 Job에는 1개 이상의 Step이 존재해야 한다.</li>
</ul>
<h3>StepExecution</h3>
<ul>
  <li>Job에 JobExecution이라는 Job 실행 정보가 있다면 Step에는 StepExecution이라는 Step 실행 정보를 담는 객체가 있다.</li>
</ul>
<h3>JobRepository</h3>
<ul>
  <li>JobRepository는 배치 처리 정보를 담고 있는 매커니즘이다. 어떤 Job이 실행되었으면 몇번 실행되었고 언제 끝났는지 등의 배치 처리에 대한 메타데이터를 저장한다.</li>
  <li>Job 하나가 실행되면 JobRepository에서는 배치 실행에 관련된 정보를 담고 있는 도메인 JobExecution을 생성한다.</li>
  <li>Step의 실행 정보를 담고 있는 StepExecution도 저장소에 저장하여 전체 메타데이터를 저장/관리하는 역할을 수행한다.</li>
</ul>
<h3>JobLauncher</h3>
<ul>
  <li>Job, JobParameters와 함께 배치를 실행하는 인터페이스이다.</li>
</ul>
<h3>ItemReader</h3>
<ul>
  <li>Step의 대상이 되는 배치 데이터를 읽어오는 인터페이스이다. File, Xml, DB 등 여러 타입의 데이터를 읽어올 수 있다.</li>
</ul>
<h3>ItemProcessor</h3>
<ul>
  <li>Itemreader로 읽어 온 배치 데이터를 변환하는 역할을 수행한다.</li>
  <li>
    읽어온 배치 데이터와 씌여질 데이터의 타입이 다를 경우에 대응할 수 있도록 비지니스 로직을 분리한다.
    <ul>
      <li>ItemWriter는 저장을 수행하며 ItemProcessor는 로직 처리만 수행한다.</li>
    </ul>
  </li>
</ul>
<h3>ItemWriter</h3>
<ul>
  <li>배치 데이터를 저장한다. 일반적으로 DB나 파일에 저장한다.</li>
  <li>ItemReader와 비슷한 방식으로 구현한다. 재네릭으로 원하는 타입을 받고 write() 메소드를 통해 List를 사용해서 저장한 타입의 목록을 매개변수로 받는다.</li>
</ul>

## 레퍼런스
https://cheese10yun.github.io/spring-batch-basic/
