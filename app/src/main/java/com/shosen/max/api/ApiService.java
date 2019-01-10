package com.shosen.max.api;

import com.shosen.max.bean.AlipayReqBean;
import com.shosen.max.bean.CommentBean;
import com.shosen.max.bean.ContributeListBean;
import com.shosen.max.bean.DictionaryBean;
import com.shosen.max.bean.FileUploadResponse;
import com.shosen.max.bean.FriendCircleBean;
import com.shosen.max.bean.FriendInvitationBean;
import com.shosen.max.bean.GetCountByUserIdBean;
import com.shosen.max.bean.GoodsDetailBean;
import com.shosen.max.bean.IsBookPayResponse;
import com.shosen.max.bean.MallAddressBean;
import com.shosen.max.bean.MallHomePageBean;
import com.shosen.max.bean.LocationBean;
import com.shosen.max.bean.LotteryResponse;
import com.shosen.max.bean.LuckyPanReward;
import com.shosen.max.bean.MoneyListResponse;
import com.shosen.max.bean.MyRessMessBean;
import com.shosen.max.bean.NewsBean;
import com.shosen.max.bean.OrderPayResponseOutter;
import com.shosen.max.bean.OrderReChargeResponse;
import com.shosen.max.bean.OrderResponse;
import com.shosen.max.bean.BaseResponse;
import com.shosen.max.bean.LoginResponse;
import com.shosen.max.bean.OrderSubmitResponse;
import com.shosen.max.bean.RelationBean;
import com.shosen.max.bean.RemainNumResponse;
import com.shosen.max.bean.RewardListBean;
import com.shosen.max.bean.RewardTotalResponse;
import com.shosen.max.bean.ShoppingCartBean;
import com.shosen.max.bean.TableBean;
import com.shosen.max.bean.User;
import com.shosen.max.bean.UserDetetail;
import com.shosen.max.bean.VerifyCodeResponse;
import com.shosen.max.bean.VersionUpdateResponse;
import com.shosen.max.bean.WxPayBean;
import com.shosen.max.bean.mall.GoodsOrderList;
import com.shosen.max.bean.mall.MallOrderDetailsResponse;

import java.util.List;
import java.util.Map;


import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface ApiService {

    public static final String BASE_URL = "http://192.168.1.160:8092/";

    //请求验证码
    @POST("login/code")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<VerifyCodeResponse>> loginCode(@Body RequestBody requestBody);

    //登出
    @POST("login/logoout")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> logoOut(@Body RequestBody requestBody);

    //登陆验证
    @POST("login/validate")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<LoginResponse>> loginValidate(@Body RequestBody requestBody);

    //用户预定
    @POST("user/book")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> userBook(@Body RequestBody requestBody);

    //预定详情
    @POST("user/bookDetail")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<OrderResponse>> userBookDetail(@Body RequestBody requestBody);

    //预定列表
    @POST("user/bookList")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<OrderResponse>>> userBookList(@Body RequestBody requestBody);

    //支付订单
    @POST("user/payBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> payBook(@Body RequestBody requestBody);

    //取消订单
    @POST("user/cancelBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> cancelBook(@Body RequestBody requestBody);

    //删除订单
    @POST("user/delBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> delBook(@Body RequestBody requestBody);

    //预定
    @POST("user/book")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<OrderResponse>> book(@Body RequestBody requestBody);

    //检查更新
    @POST("v/upgrade")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> checkUpdate(@Body RequestBody requestBody);

    //更新用户信息
    @POST("user/editUser")
    @Multipart
    Observable<BaseResponse<UserDetetail>> updateUserInfo(@PartMap() Map<String, RequestBody> partMap,
                                                          @Part @Nullable MultipartBody.Part file);

    //更新用户信息 不修改头像
    @POST("user/editUser")
    @Multipart
    Observable<BaseResponse<UserDetetail>> updateUserInfo(@PartMap() Map<String, RequestBody> partMap);

    //获取微信支付信息
    @POST("user/appWxPayBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<WxPayBean>> invokeWxPay(@Body RequestBody requestBody);

    //获取支付宝支付信息
    @POST("user/appAliPayBook")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<AlipayReqBean>> invokeAliPay(@Body RequestBody requestBody);

    //津贴领取列表
    @POST("user/rewardList")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<RewardListBean>>> rewardList(@Body RequestBody requestBody);

    //津贴汇总信息
    @POST("user/rewardTotal")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<RewardTotalResponse>> rewardTotal(@Body RequestBody requestBody);

    //根据Phone查询用户
    @POST("user/selectUserByPhone")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<User>> selectUserByPhone(@Body RequestBody requestBody);

    //查找字典数据 type=12抽奖数据
    @GET("base/dictionary")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<DictionaryBean>>> getDictionary(@Query("type") String type);

    //(未登录时查询)剩余车主数
    @GET("base/remainOwner")
    Observable<BaseResponse<RemainNumResponse>> getRemainNum();

    //检查版本
    @POST("v/upgrade")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<VersionUpdateResponse>> getLatestVerison(@Body RequestBody requestBody);

    //微信绑定(不需要验证码)
    @POST("weChat/loginRegister")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> weChatLoginRegister(@Body RequestBody requestBody);

    //微信注册
    @POST("weChat/login")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> weChatLogin(@Body RequestBody requestBody);

    //微信绑定
    @POST("weChat/register")
    Observable<BaseResponse<LoginResponse>> weChatRegister(@Body RequestBody requestBody);

    //微信解绑
    @POST("base/weChatUnLock")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> weChatUnLock(@Body RequestBody requestBody);

    //朋友圈数据
    @POST("circle/selectMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendCircleBean>>> selectFriendCircle(@Body RequestBody requestBody);

    //刷新朋友圈数据
    @POST("circle/refreshMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> refreshMess(@Body RequestBody requestBody);

    //发布朋友圈消息
    @POST("circle/addMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> addMess(@Body RequestBody requestBody);

    //删除朋友圈消息
    @POST("circle/delMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> delMess(@Body RequestBody requestBody);


    //获取评论列表
    @POST("circle/selectComm")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<CommentBean>>> getComments(@Body RequestBody requestBody);

    //添加评论
    @POST("circle/addComment")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> addComment(@Body RequestBody requestBody);

    //删除评论
    @POST("circle/delComment")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> delComment(@Body RequestBody requestBody);

    //添加关注
    @POST("circle/addFollow")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> addFollow(@Body RequestBody requestBody);

    //取消关注
    @POST("circle/delFollow")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> delFollow(@Body RequestBody requestBody);

    //关注列表
    @POST("circle/followList")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> getFollowList(@Body RequestBody requestBody);

    //上传一张图片
    @POST("base/upload")
    @Multipart
    Observable<BaseResponse<FileUploadResponse>> baseUpLoad(@Part @Nullable MultipartBody.Part file);

    //上传多张图片
    @POST("base/uploadFiles")
    @Multipart
    Observable<BaseResponse<FileUploadResponse>> baseUpLoadFiles(@Part @Nullable MultipartBody.Part... files);

    //获取BANNER数据
    @GET("base/bannerList")
    Observable<BaseResponse<List<String>>> getBannerList(@Query("type") int type);

    //好友推荐
    @POST("circle/friendInvi")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendInvitationBean>>> getFriendInvitation(@Body RequestBody requestBody);

    //点赞
    @POST("circle/updateMessMark")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> updateMessMark(@Body RequestBody requestBody);

    //评论点赞
    @POST("circle/updateCommentMark")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> updateCommentMark(@Body RequestBody requestBody);

    //热门话题
    @POST("circle/hotTop")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendCircleBean>>> gethotTop(@Body RequestBody requestBody);

    //个人关注
    @POST("circle/selfFollow")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendInvitationBean>>> selfFollow(@Body RequestBody requestBody);

    //个人消息
    @POST("circle/selfMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendCircleBean>>> selfMess(@Body RequestBody requestBody);

    //更多关注
    @POST("circle/moreFollow")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> moreFollower(@Body RequestBody requestBody);

    //获取个人中心 朋友圈 粉丝 关注数量接口
    @POST("circle/getCountByUserId")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<GetCountByUserIdBean>> getCountByUserId(@Body RequestBody requestBody);

    //我的消息列表
    @POST("circle/myReMesss")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<MyRessMessBean>>> getMyReMesss(@Body RequestBody requestBody);

    //举报拉黑  
    @POST("circle/defriendAndComplain")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> defriendAndComplain(@Body RequestBody requestBody);

    //取消拉黑
    @POST("circle/cancelDefriend")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> cancelDefriend(@Body RequestBody requestBody);

    //粉丝列表
    @POST("circle/selectFans")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<FriendInvitationBean>>> getMyFans(@Body RequestBody requestBody);

    //判断用户关系
    @POST("circle/isRelation")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<RelationBean>> isRelation(@Body RequestBody requestBody);

    //消息提醒
    @POST("circle/noticeMess")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<MyRessMessBean>>> getNoticeMess(@Body RequestBody requestBody);

    //签到
    @POST("base/signLogin")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<String>> signLogin(@Body RequestBody requestBody);

    //提交问卷
    @POST("base/subjectAns")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> subjectAns(@Body RequestBody requestBody);


    //贡献值列表接口
    @GET("base/contriList")
    Observable<BaseResponse<List<ContributeListBean>>> getContributeList(@Query("phone") String phone
            , @Query("pageNum") int pageNum);

    //当天贡献值
    @GET("base/contriToday")
    Observable<BaseResponse<List<ContributeListBean>>> getContributeToday(@Query("phone") String phone);

    //获取机密后的手机号
    @GET("base/encodePho")
    Observable<BaseResponse<String>> getEncodedPhone(@Query("phone") String phone);

    //快速购买
    @POST("wx/cart/fastadd")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> fastBuy(@Body RequestBody requestBody);


    /**
     * 所有获奖名单
     *
     * @param phone
     * @return
     */
    @GET("game/rewardRecord")
    Observable<BaseResponse<List<LuckyPanReward>>> getPanRewardList(@Query("phone") String phone);

    //TODO 修改
    //我的奖品
    @POST("game/rewardList")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<List<LuckyPanReward>>> getMineRewardList(@Body RequestBody requestBody);

    //转盘抽奖
    @GET("game/reward")
    Observable<BaseResponse<LotteryResponse>> lottery(@Query("phone") String phone);

    //抽奖剩余次数
    @GET("game/intReTime")
    Observable<BaseResponse<String>> getLeftLotteryTime(@Query("phone") String phone);

    //资产明细
    @GET("base/xProList")
    Observable<BaseResponse<List<TableBean>>> getProList(@Query("phone") String phone);

    @GET("base/news")
    Observable<BaseResponse<List<NewsBean>>> getNews();

    //获取需要定位的地址
    @GET("base/posiKeyVal")
    Observable<BaseResponse<List<LocationBean>>> getLocations();

    //发送提示信息接口
    @GET("base/posiSms")
    Observable<BaseResponse> postPositionSms(@Query("phone") String phone,
                                             @Query("code") String code);


    //商城首页
    @GET("wx/home/index")
    Observable<BaseResponse<MallHomePageBean>> hallHomeIndex();

    //商品详情
    @GET("wx/goods/detail")
    Observable<BaseResponse<GoodsDetailBean>> goodsDetails(@Query("id") String id);

    //加入购物车
    @POST("wx/cart/add")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> addToShoppingCart(@Body RequestBody requestBody);


    //立即购买
    @POST("wx/cart/fastadd")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<String>> payNow(@Body RequestBody requestBody);

    //购物车列表
    @GET("wx/cart/index")
    //@Headers("Content-Type: application/json")
    Observable<BaseResponse<ShoppingCartBean>> cartIndex(@Query("userId") String userName);

    //删除购物车栏目
    @POST("wx/cart/delete")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> cartDelete(@Body RequestBody requestBody);

    //购物车加减号修改
    @POST("wx/cart/update")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> cartUpdate(@Body RequestBody requestBody);

    //地址列表
    @GET("wx/address/list")
    Observable<BaseResponse<List<MallAddressBean>>> addressList(@Query("userId") String userName);

    //修改地址 保存新地址
    @POST("wx/address/save")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> addressSave(@Body RequestBody requestBody);

    //删除地址
    @POST("wx/address/delete")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> addressDel(@Body RequestBody requestBody);

    //购物车数量
    @GET("wx/cart/goodscount")
    Observable<BaseResponse<Integer>> getGoodsCount(@Query("userId") String userId);

    //提交订单
    @POST("wx/order/submit")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<OrderSubmitResponse>> submitOrder(@Body RequestBody requestBody);

    //修改购物车选取状态
    @POST("wx/cart/checked")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> cartChecked(@Body RequestBody requestBody);

    //订单列表
    @GET("wx/order/list")
    Observable<BaseResponse<List<GoodsOrderList>>> mallOrderList(@Query("userId") String userId, @Query("showType") int showType);

    //订单详情
    @GET("wx/order/detail")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<MallOrderDetailsResponse>> mallOrderDetails
    (@Query("userId") String userId, @Query("orderId") String orderId);

    //取消订单
    @POST("wx/order/cancel")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> cancelOrder(@Body RequestBody requestBody);

    //删除订单
    @POST("wx/order/delete")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> deleteOrder(@Body RequestBody requestBody);

    //确认收货
    @POST("wx/order/confirm")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> confirmOrder(@Body RequestBody requestBody);

    //是否购买过车
    @GET("base/isBookPay")
    Observable<BaseResponse<IsBookPayResponse>> isBookPay(@Query("phone") String phone);

    //激活社区
    @POST("base/socialPass")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse> activeCommunity(@Body RequestBody requestBody);

    @POST("wx/order/prepay")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<OrderPayResponseOutter>> mallOrderPrePay(@Body RequestBody requestBody);

    @GET("base/moneyList")
    Observable<BaseResponse<List<MoneyListResponse>>>
    moneyList(@Query("phone") String phone, @Query(value = "type", encoded = true) String type, @Query("pageNum") int pageNum);

    @GET("base/xProList")
    Observable<BaseResponse<List<MoneyListResponse>>>
    xProList(@Query("phone") String phone, @Query(value = "type", encoded = true) String type, @Query("pageNum") int pageNum);

    //我的社区 验证 验证码
    @POST("user/updatePass")
    Observable<BaseResponse> updatePass(@Body RequestBody requestBody);

    //生成充值订单
    @POST("user/charge")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<OrderReChargeResponse>> getUserCharge(@Body RequestBody requestBody);

    @POST("user/wxPayCharge")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<WxPayBean>> invokeWxPayCharge(@Body RequestBody requestBody);


    @POST("user/aliPayCharge")
    @Headers("Content-Type: application/json")
    Observable<BaseResponse<AlipayReqBean>> invokeAliPayCharge(@Body RequestBody requestBody);

    @GET("wx/order/findList")
    Observable<BaseResponse<List<GoodsOrderList>>> findOrderList(@Query(value = "userId") String userId,
                                                                 @Query(value = "showType") int showType,
                                                                 @Query(value = "goodsName") String goodsName);


    @GET("{url}")
    Observable<BaseResponse<Object>> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> maps
    );


    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            //  @Header("") String authorization,
            @QueryMap Map<String, String> maps);


    @POST("{url}")
    @Headers("Content-Type: application/json")
    Observable<ResponseBody> executePost(
            @Path(value = "url", encoded = true) String url,
            @Body RequestBody requestBody);

    @POST("{url}")
    Observable<ResponseBody> json(
            @Path("url") String url,
            @Body RequestBody jsonStr);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Path("headers") Map<String, String> headers,
            @Part("filename") String description,
            @PartMap() Map<String, RequestBody> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);


}
