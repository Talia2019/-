import React, { Component } from "react";
import "./StreamComponent.css";
import OvVideoComponent from "./OvVideo";

import MicOff from "@material-ui/icons/MicOff";
import VideocamOff from "@material-ui/icons/VideocamOff";
import VolumeUp from "@material-ui/icons/VolumeUp";
import VolumeOff from "@material-ui/icons/VolumeOff";
import FormControl from "@material-ui/core/FormControl";
import Input from "@material-ui/core/Input";
import InputLabel from "@material-ui/core/InputLabel";
import IconButton from "@material-ui/core/IconButton";
import HighlightOff from "@material-ui/icons/HighlightOff";
import FormHelperText from "@material-ui/core/FormHelperText";

export default class StreamComponent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      nickname: this.props.user.getNickname(),
      showForm: false,
      mutedSound: false,
      isFormValid: true,
    };
    this.handleChange = this.handleChange.bind(this);
    this.handlePressKey = this.handlePressKey.bind(this);
    this.toggleNicknameForm = this.toggleNicknameForm.bind(this);
    this.toggleSound = this.toggleSound.bind(this);
  }

  handleChange(event) {
    this.setState({ nickname: event.target.value });
    event.preventDefault();
  }

  toggleNicknameForm() {
    if (this.props.user.isLocal()) {
      this.setState({ showForm: !this.state.showForm });
    }
  }

  toggleSound() {
    this.setState({ mutedSound: !this.state.mutedSound });
  }

  handlePressKey(event) {
    if (event.key === "Enter") {
      console.log(this.state.nickname);
      if (this.state.nickname.length >= 3 && this.state.nickname.length <= 20) {
        this.props.handleNickname(this.state.nickname);
        this.toggleNicknameForm();
        this.setState({ isFormValid: true });
      } else {
        this.setState({ isFormValid: false });
      }
    }
  }

  render() {
    return (
      <div className="OT_widget-container">
        {this.props.user !== undefined && this.props.user.getStreamManager() !== undefined ? (
          <div className="streamComponent">
            {/* {this.props.user.isAudioActive()?():()} */}
            <OvVideoComponent user={this.props.user} mutedSound={this.state.mutedSound} />
            {this.props.user.isVideoActive() ? (
              <div className="video-username">{this.state.nickname}</div>
            ) : (
              <div className="video-camoff">{this.state.nickname}</div>
            )}
            <div className="video-time">02:48</div>

            <div id="status-icons">
              {/* {!this.props.user.isVideoActive() ? (
                <div id="camIcon">
                  <VideocamOff id="statusCam" />
                </div>
              ) : null} */}

              {!this.props.user.isAudioActive() ? (
                <div id="micIcon">
                  <svg
                    width="30"
                    height="30"
                    viewBox="0 0 20 20"
                    fill="none"
                    xmlns="http://www.w3.org/2000/svg"
                  >
                    <g clip-path="url(#clip0_1140_5144)">
                      <path
                        d="M528 448H112C103.2 448 96 455.2 96 464V496C96 504.8 103.2 512 112 512H528C536.8 512 544 504.8 544 496V464C544 455.2 536.8 448 528 448ZM592 128C565.5 128 544 149.5 544 176C544 183.1 545.6 189.7 548.4 195.8L476 239.2C460.6 248.4 440.7 243.2 431.8 227.6L350.3 85C361 76.2 368 63 368 48C368 21.5 346.5 0 320 0C293.5 0 272 21.5 272 48C272 63 279 76.2 289.7 85L208.2 227.6C199.3 243.2 179.3 248.4 164 239.2L91.7 195.8C94.4 189.8 96.1 183.1 96.1 176C96.1 149.5 74.6 128 48.1 128C21.6 128 0 149.5 0 176C0 202.5 21.5 224 48 224C50.6 224 53.2 223.6 55.7 223.2L128 416H512L584.3 223.2C586.8 223.6 589.4 224 592 224C618.5 224 640 202.5 640 176C640 149.5 618.5 128 592 128Z"
                        fill="black"
                      />
                      <path
                        d="M1.70703 1.70728L17.9999 18.0002"
                        stroke="#FF4C4C"
                        stroke-linecap="round"
                      />
                      <path
                        d="M15.0015 17.5002H13.7643C13.0268 17.5185 12.3077 17.2686 11.7405 16.7969C11.1732 16.3253 10.7964 15.6638 10.6798 14.9354C11.5411 14.7759 12.3196 14.3203 12.8804 13.6472C13.4412 12.9742 13.7489 12.1263 13.7502 11.2502V5.00024C13.7502 4.00568 13.3552 3.05186 12.6519 2.34859C11.9486 1.64533 10.9948 1.25024 10.0002 1.25024C9.00568 1.25024 8.05186 1.64533 7.3486 2.34859C6.64533 3.05186 6.25025 4.00568 6.25025 5.00024V11.2502C6.2518 12.1428 6.57145 13.0056 7.15181 13.6837C7.73217 14.3618 8.53524 14.8108 9.41685 14.9502C9.53458 15.9168 9.97518 16.8153 10.6673 17.5002H5.00147C4.8357 17.5002 4.67673 17.5661 4.55952 17.6833C4.44231 17.8005 4.37646 17.9595 4.37646 18.1252C4.37646 18.291 4.44231 18.45 4.55952 18.5672C4.67673 18.6844 4.8357 18.7502 5.00147 18.7502H15.0015C15.1672 18.7502 15.3262 18.6844 15.4434 18.5672C15.5606 18.45 15.6265 18.291 15.6265 18.1252C15.6265 17.9595 15.5606 17.8005 15.4434 17.6833C15.3262 17.5661 15.1672 17.5002 15.0015 17.5002ZM7.50025 9.36774V8.12524H12.5002V9.36774H7.50025ZM10.0002 2.50024C10.6631 2.50091 11.2986 2.76451 11.7673 3.23321C12.236 3.70191 12.4996 4.33741 12.5002 5.00024V6.87524H7.50025V5.00024C7.50091 4.33741 7.76451 3.70191 8.23321 3.23321C8.70191 2.76451 9.33741 2.50091 10.0002 2.50024Z"
                        fill="#FF4D4D"
                      />
                      <path
                        d="M7.5 9.375H12.5V11.25C12.5 12.6307 11.3807 13.75 10 13.75C8.61929 13.75 7.5 12.6307 7.5 11.25V9.375Z"
                        fill="#FF4D4D"
                      />
                      <rect x="7.5" y="6.875" width="5" height="1.25" fill="#FF4D4D" />
                    </g>
                    <defs>
                      <clipPath id="clip0_1140_5144">
                        <rect width="20" height="20" fill="white" />
                      </clipPath>
                    </defs>
                  </svg>
                </div>
              ) : null}
            </div>
            <div>
              {!this.props.user.isLocal() && (
                <IconButton id="volumeButton" onClick={this.toggleSound}>
                  {this.state.mutedSound ? <VolumeOff color="secondary" /> : <VolumeUp />}
                </IconButton>
              )}
            </div>
          </div>
        ) : null}
      </div>
    );
  }
}
