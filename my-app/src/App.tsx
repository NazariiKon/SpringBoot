import "cropperjs/dist/cropper.min.css";
import * as React from 'react';
import logo from './logo.svg';
import './App.css';
import { Modal, Button, Col, Row } from 'antd';
import Cropper from "cropperjs";



function App() {
  const [visible, setVisible] = React.useState(false);
  const imgRef = React.useRef<HTMLImageElement>(null);
  const prevRef = React.useRef<HTMLImageElement>(null);
  const [cropperObj, setCropperObj] = React.useState<Cropper>();
  const [imgShow, setImgShow] = React.useState("https://media.75.ru//resources/214406/363a7af9b893f819e366cba3f8d28f79e709afc2%D1%84%D0%BE%D1%82%D0%BE%20%D0%BA%D0%BE%D1%82%20%D0%BC%D0%B0%D0%BD%D1%83%D0%BB.jpg");

  const hadlerShowCropper = async () => {
    await setVisible(true);
    let cropper = cropperObj; 
    
    if (!cropper) {
      cropper = new Cropper(imgRef.current as HTMLImageElement, {
        aspectRatio: 1 / 1,
        viewMode: 1,
        preview: prevRef.current as HTMLImageElement,
      });
    }
    setCropperObj(cropper);
  }

  const handlerCroppedImg= () => {
    const base64 = cropperObj?.getCroppedCanvas().toDataURL() as string;
    console.log("base64", base64);
    setVisible(false);
  }

  return (
    <>
      <h1>Hello react</h1>
      <Button type="primary" onClick={hadlerShowCropper}>
        Open Modal of 1000px width
      </Button>

      <Modal
        title="Modal 1000px width"
        centered
        visible={visible}
        onOk={handlerCroppedImg}
        onCancel={() => setVisible(false)}
        width={1000}
      >
        <Row gutter={[8, 8]}>
          <Col md={18} xs={24}>
            <img
              ref={imgRef}
              src="https://vovalohika.tk/images/1200_gntox1zw.ipw.jpeg"
              width="100%"
            />
          </Col>
          <Col md={6} xs={24}>
            <div
              ref={prevRef}
              style={{
                height: "150px",
                border: "1px solid silver",
                overflow: "hidden",
              }}
            ></div>
          </Col>
        </Row>
      </Modal>
    </>
  );
}

export default App;