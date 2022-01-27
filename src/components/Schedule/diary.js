import { Row, Col } from 'react-bootstrap';
import { useEffect, useState } from 'react';
import axios from 'axios';

export default function Diary() {
  const URL = 'https://api.thecatapi.com/v1/images/search';
  const [catURL, setCatURL] = useState('');
  const [maxim, setMaxim] = useState('');
  const getCatImg = async () => {
    axios
      .get(URL)
      .then((response) =>
        setCatURL(response.data[0]['url'])
      );
  };
  const getMaxim = async () => {
    axios
      .get('https://api.adviceslip.com/advice')
      .then((response) =>
        setMaxim(response.data['slip']['advice'])
      );
  };

  useEffect(() => {
    getMaxim();
    getCatImg();
  }, []);

  return (
    <div>
      <h3>diary</h3>
      <Row>
        <Col>
          <img
            src={catURL}
            style={{ width: '15vw' }}
            alt=""
          />
        </Col>
        <Col>Today's maxim: {maxim}</Col>
      </Row>
    </div>
  );
}
