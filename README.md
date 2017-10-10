# AndroidVideoPlayer

    本项目，简单的实现和集成了Android系统下常见的视频播放器；
       包括：videoview
            Surfaceview
            Vitamio集成使用
            Bilibili框架集成
    
    1.VideoView 实现播放效果 
    
         vv = (VideoView) findViewById(R.id.vv);
                final File file = new File("/storage/emulated/0/Movies/Starry_Night.mp4");
        
                // 创建一个MediaController。
                // 这里的Context一定要传入this，不能用getApplicationContext().否则一点击屏幕就闪退
                mc = new MediaController(this);
        
                // 开创建SurfaceView的控制器
                SurfaceHolder holder = vv.getHolder();
        
                holder.addCallback(new SurfaceHolder.Callback() {
        
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
        
                        if (file.exists()) {
                            // 设置要播放的视频
                            vv.setVideoPath(file.getPath());
        
                            // 设置VideoView与MediaController相关联
                            vv.setMediaController(mc);
        
                            // 设置VideoView获取焦点
                            vv.requestFocus();
        
                            // 将背景图片设为透明
                            // vv.getBackground().setAlpha(0);
        
                            // 开始播放视频（如果上次有播放记录，接着上次播放）
                            vv.start();
                            vv.seekTo(secondCurrentPosition);
        
                        } else {
                            Toast.makeText(getApplicationContext(), "视频不存在", Toast.LENGTH_SHORT).show();
                        }
        
                    }
        
                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    }
        
        
                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                    }
                });
        
                // 设置播放完毕后循环播放
                vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.seekTo(0);
                        mp.start();
                    }
                });
        
                // 增加监听上一个和下一个的切换事件，默认这两个按钮是不显示的
                mc.setPrevNextListeners(new View.OnClickListener() {
        
                    @Override
                    public void onClick(View v) {
                        // 这里直接弹出吐司，开发中这里应当切换到下一首
                        Toast.makeText(VideoViewActivity.this, "下一个", Toast.LENGTH_SHORT).show();
                    }
                }, new View.OnClickListener() {
        
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(VideoViewActivity.this, "上一个", Toast.LENGTH_SHORT).show();
                    }
                });
        
            }
        
            //当Activity退出界面(无论Activity是否销毁了)，记录当前视频播放进度
            @Override
            protected void onPause() {
                if (vv.isPlaying()) {
                    secondCurrentPosition = vv.getCurrentPosition();
                    vv.pause();
                    vv.suspend();
                }
        
                super.onPause();
            }

    
    2.SurfaceView + MediaPlayer（详情见代码，这里是第一种方法，不带进度条和控制栏的）
        
         vv = (VideoView) findViewById(R.id.vv);
                final File file = new File("/storage/emulated/0/Movies/Starry_Night.mp4");
        
                // 创建一个MediaController。
                // 这里的Context一定要传入this，不能用getApplicationContext().否则一点击屏幕就闪退
                mc = new MediaController(this);
        
                // 开创建SurfaceView的控制器
                SurfaceHolder holder = vv.getHolder();
        
                holder.addCallback(new SurfaceHolder.Callback() {
        
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
        
                        if (file.exists()) {
                            // 设置要播放的视频
                            vv.setVideoPath(file.getPath());
        
                            // 设置VideoView与MediaController相关联
                            vv.setMediaController(mc);
        
                            // 设置VideoView获取焦点
                            vv.requestFocus();
        
                            // 将背景图片设为透明
                            // vv.getBackground().setAlpha(0);
        
                            // 开始播放视频（如果上次有播放记录，接着上次播放）
                            vv.start();
                            vv.seekTo(secondCurrentPosition);
        
                        } else {
                            Toast.makeText(getApplicationContext(), "视频不存在", Toast.LENGTH_SHORT).show();
                        }
        
                    }
        
                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    }
        
        
                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                    }
                });
        
                // 设置播放完毕后循环播放
                vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.seekTo(0);
                        mp.start();
                    }
                });
        
                // 增加监听上一个和下一个的切换事件，默认这两个按钮是不显示的
                mc.setPrevNextListeners(new View.OnClickListener() {
        
                    @Override
                    public void onClick(View v) {
                        // 这里直接弹出吐司，开发中这里应当切换到下一首
                        Toast.makeText(VideoViewActivity.this, "下一个", Toast.LENGTH_SHORT).show();
                    }
                }, new View.OnClickListener() {
        
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(VideoViewActivity.this, "上一个", Toast.LENGTH_SHORT).show();
                    }
                });
        
            }
        
            //当Activity退出界面(无论Activity是否销毁了)，记录当前视频播放进度
            @Override
            protected void onPause() {
                if (vv.isPlaying()) {
                    secondCurrentPosition = vv.getCurrentPosition();
                    vv.pause();
                    vv.suspend();
                }
        
                super.onPause();
            }
    
    3.Vitamio 第三方框架
         //检查vitamio框架是否可用
               /* if (!LibsChecker.checkVitamioLibs(this)) {
                    return;
                }*/
        
                setContentView(R.layout.activity_vitamio);
                ButterKnife.bind(this);
        
                //显示缓冲百分比的TextView
                percentTv = (TextView) findViewById(R.id.buffer_percent);
                //显示下载网速的TextView
                netSpeedTv = (TextView) findViewById(R.id.net_speed);
        
                //初始化加载库文件
                if (Vitamio.isInitialized(this)) {
                    videoView = (VideoView) findViewById(R.id.vitamio);
                    videoView.setVideoURI(Uri.parse(url1));
                    videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
        
                    MediaController controller = new MediaController(this);
                    videoView.setMediaController(controller);
                    videoView.setBufferSize(10240); //设置视频缓冲大小。默认1024KB，单位byte
                    videoView.requestFocus();
        
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            // optional need Vitamio 4.0
                            mediaPlayer.setPlaybackSpeed(1.0f);
                            //mediaPlayer.setLooping(true);
                        }
                    });
        
                    videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                            percentTv.setText("已缓冲：" + percent + "%");
                        }
        
                    });
        
                    videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            switch (what) {
                                //开始缓冲
                                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                    percentTv.setVisibility(View.VISIBLE);
                                    netSpeedTv.setVisibility(View.VISIBLE);
                                    mp.pause();
                                    break;
                                //缓冲结束
                                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                    percentTv.setVisibility(View.GONE);
                                    netSpeedTv.setVisibility(View.GONE);
                                    mp.start(); //缓冲结束再播放
                                    break;
                                //正在缓冲
                                case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                                    netSpeedTv.setText("当前网速:" + extra + "kb/s");
                                    break;
                            }
        
                            return true;
                        }
                    });
                }
                
        
    