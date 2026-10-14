<h1 align="center">안성상회 메인 서버</h1>
<h3 align="center">저희 서비스의 중심. 메인 서버입니다.</h3>

<br><br><br>
<div align=center>
<img src="images/image.png" width=575></img>
<video src="images/video.mp4"></video>
</div>
<br><br><br>


모놀리식 아키텍처로 운영중인 메인 애플리케이션입니다.
이 애플리케이션은 GraalVM 네이티브 이미지로 빌드하여 배포하고 있으며, Cold-Start가 고질병인 Spring 애플리케이션에서 1초 내외의 부팅 속도를 자랑하며 EC2 등의 온디멘드 컴퓨팅 외 Serverless 까지 다양한 아키텍처 선택지가 있습니다.
GraalVM 네이티브 이미지 빌드 및 배포 성공을 거점으로 점진적으로 MSA 아키텍처로 마이그레이션을 제정상황에 맞게 진행중에 있습니다.

프로젝트는 DDD 기반 아키텍처를 적용하였으며 전역 설정은 global 디렉토리에서 확인하실 수 있습니다.
