import org.junit.jupiter.api.Test;

// gradle에서는 기본적으로 src/test/java 폴더를 test파일의 소스경로로 인식
// : java 포더 내의 하위 테스트 클래스에는 static import를 사용하여
// : Assertions의 메서드를 직접 호출
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class AssertJTest {
    @Test
    public void assertJTest() {
        String name1 = "박영준";
        String name2 = "홍길동";
        String name3 = "전우치";

        // 모든 변수가 NULL이 아닌지 검증
        assertThat(name1).isNotNull();
        assertThat(name2).isNotNull();
        assertThat(name3).isNotNull();

        // name1 과 name2가 같은지 확인
        assertThat(name1).isEqualTo(name2);
        // name1 과 name3이 다른지 확인
        assertThat(name1).isNotEqualTo(name3);

    }
}
