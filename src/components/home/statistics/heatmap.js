import React, { useEffect, useState } from "react";
import ReactCalendarHeatmap from "react-calendar-heatmap";
// import "react-calendar-heatmap/dist/styles.css";
import "../../../statics/css/home/heatmap.css";
import ReactTooltip from "react-tooltip";
import axios from "axios";

function Heatmap() {
  const [yearData, setYearData] = useState("");
  function shiftDate(date, numDays) {
    const newDate = new Date(date);
    newDate.setDate(newDate.getDate() + numDays);
    return newDate;
  }
  useEffect(() => {
    axios
      .get("/stats/year", {
        headers: {
          Authorization: `Bearer ` + localStorage.getItem("accessToken"),
        },
      })
      .then((res) => {
        console.log(res);
        if (res.data.statusCode === 200) {
          setYearData(() => res.data.result);
        }
      });
  }, []);

  // 데이터 받아오면 지울 부분
  function getRange(count) {
    return Array.from({ length: count }, (_, i) => i);
  }

  function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }
  const today = new Date();
  const randomValues = getRange(2000).map((index) => {
    console.log(index);
    return {
      date: shiftDate(today, -index),
      count: getRandomInt(0, 3),
    };
  });
  // 여기까지 지우면 됨

  return (
    <div className="heatmap">
      <ReactCalendarHeatmap
        startDate={shiftDate(today, -362)}
        endDate={today}
        values={randomValues}
        classForValue={(value) => {
          if (!value) {
            return "color-empty";
          }
          return `color-beammp-${value.count}`;
        }}
        tooltipDataAttrs={(value) => {
          return {
            "data-tip": `${value.date.toISOString().slice(0, 10)} has count: ${
              value.count
            }`,
          };
        }}
        showOutOfRangeDay={true}
        showWeekdayLabels={false}
        onClick={(value) =>
          alert(`Clicked on value with count: ${value.count}`)
        }
      />
      <ReactTooltip />
    </div>
  );
}
export default Heatmap;
